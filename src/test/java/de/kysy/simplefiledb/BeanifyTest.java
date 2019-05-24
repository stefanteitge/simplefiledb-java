/*
 * This file is part of SimpleFileDB.
 *
 * SimpleFileDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SimpleFileDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SimpleFileDB.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.kysy.simplefiledb;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class BeanifyTest {

	@Test
	public void testBeanify() {
		IDatabase database = null;
		try {
			database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		} catch (DatabaseException e1) {
			Assert.fail("Caught exception during opening database");
		}

		SimpleBean bean = null;
		try {
			ITable table = database.getTable("e", false);
			bean = table.getAll()[0].beanify(SimpleBean.class);
		} catch (DatabaseException e) {
			Assert.fail("Caught exception during beanify");
		}
		
		Assert.assertEquals("Expected correct id", "42", bean.getId());
		Assert.assertEquals("Expected correct id", "Anna", bean.getName());
	}
}
