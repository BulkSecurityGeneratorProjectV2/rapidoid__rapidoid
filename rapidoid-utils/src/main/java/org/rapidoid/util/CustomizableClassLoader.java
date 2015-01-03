package org.rapidoid.util;

/*
 * #%L
 * rapidoid-utils
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.security.SecureClassLoader;

import org.rapidoid.lambda.Mapper;
import org.rapidoid.lambda.Predicate;

public class CustomizableClassLoader extends SecureClassLoader {

	private final Mapper<String, byte[]> classes;

	private final Predicate<String> allowed;

	private boolean forceReload;

	public CustomizableClassLoader(Mapper<String, byte[]> classes, Predicate<String> allowed, boolean forceReload) {
		this.classes = classes;
		this.allowed = allowed;
		this.forceReload = forceReload;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		U.secure(U.eval(allowed, name), "Class not allowed: %s", name);

		if (forceReload && !U.isJREClass(name)) {
			try {
				// try to force class reloading
				return findClass(name);
			} catch (ClassNotFoundException e) {
				// if the reloading fails, continue to the normal loading flow - below...
			}
		}

		try {
			// if the class has already been loaded, it's done
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			// otherwise, the super.loadClass(...) will call this.findClass(...)
			return super.loadClass(name);
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		U.secure(U.eval(allowed, name), "Class not allowed: %s", name);

		byte[] bytes = U.eval(classes, name);

		if (bytes != null) {
			return super.defineClass(name, bytes, 0, bytes.length);
		} else {
			throw new ClassNotFoundException("Cannot find class: " + name);
		}
	}

}
