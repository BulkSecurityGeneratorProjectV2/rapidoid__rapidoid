package org.rapidoid.inject.db;

/*
 * #%L
 * rapidoid-ioc
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

import org.rapidoid.inject.IoC;
import org.rapidoid.test.TestCommons;
import org.testng.annotations.Test;

public class DbInjectionTest extends TestCommons {

	@Test
	public void shouldInject() throws Exception {
		Database db = IoC.singleton(Database.class);
		isTrue(db == IoC.singleton(Database.class));
		isTrue(db == IoC.singleton(Database.class));

		notNull(db.tables);

		Table persons = db.tables.get("person");
		Table books = db.tables.get("book");

		notNullAll(persons, books);

		isTrue(persons != books);

		isTrue(persons.transactor == books.transactor);
		isTrue(persons.transactor == db.transactor);
	}

}
