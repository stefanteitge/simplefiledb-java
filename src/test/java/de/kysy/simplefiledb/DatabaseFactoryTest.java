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
package de.stefanteitge.simplefiledb;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseFactoryTest {

	@Test
	public void testGetDatabase() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);
	}
}
