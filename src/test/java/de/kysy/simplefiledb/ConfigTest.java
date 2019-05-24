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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigTest {
	
	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void testGetColums() throws DatabaseException {
		DatabaseConfig config = DatabaseConfig.createDefault();
		config.setFieldSeparator("\\ß56");
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH), config);

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("d", false);

		Assert.assertNotNull("Table may not be null", table);

		String[] columns = table.getColumns();

		Assert.assertEquals("Column count mismatch", 4, columns.length);

		Assert.assertEquals("Column name mismatch", "q", columns[0]);

		Assert.assertEquals("Column name mismatch", "r", columns[1]);

		Assert.assertEquals("Column name mismatch", "s", columns[2]);

		Assert.assertEquals("Column name mismatch", "t", columns[3]);
	}
}
