package org.rapidoid.app;

/*
 * #%L
 * rapidoid-app
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

import org.rapidoid.db.Db;
import org.rapidoid.http.HTTPInterceptor;
import org.rapidoid.http.HttpExchange;
import org.rapidoid.http.HttpInterception;
import org.rapidoid.lambda.Callback;

public class TxInterceptor implements HTTPInterceptor {

	private final Db db;

	public TxInterceptor(Db db) {
		this.db = db;
	}

	@Override
	public void intercept(HttpInterception interception) {
		final HttpExchange x = interception.exchange();

		Callback<Void> callback = new Callback<Void>() {
			@Override
			public void onDone(Void result, Throwable error) {
				if (error != null) {
					x.errorResponse(error);
				}
				x.done();
			}
		};

		x.async();
		db.transaction(interception, x.isGetReq(), callback);
	}

}
