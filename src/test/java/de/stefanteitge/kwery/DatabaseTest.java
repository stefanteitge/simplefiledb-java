/*
 * This file is part of Kwery.
 *
 * Kwery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kwery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kwery.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.stefanteitge.kwery;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

public class DatabaseTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void testGetTables() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		Assert.assertEquals("Table count mismatch", 3, database.getTables().length);
	}

	@Test
	public void testGetTable() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable tableA = database.getTable("a", false);

		Assert.assertNotNull("Table may not be null", tableA);

		ITable tableD = database.getTable("d", false);

		Assert.assertNull("Table must be null", tableD);
	}

	@Test
	public void testFlush() throws KweryException, IOException {
		File folder = temporaryFolder.newFolder();
		File source = new File(TestSettings.TEST01_PATH, "c.kwery");
		File target = new File(folder, "c.kwery");
		
		Files.copy(source, target);
		
		IDatabase database = Kwery.getDatabase(folder);

		Assert.assertNotNull("Database may not be null", database);

		ITable tableC = database.getTable("c", false);

		Assert.assertNotNull("Table may not be null", tableC);

		String value = tableC.getAll()[0].getValue("s");

		Assert.assertEquals("Read test char is invalid", value, "c");

		tableC.getAll()[0].setValue("s", "cc");

		Assert.assertEquals("Database is not modified", database.isModified(), true);

		database.flush();

		Assert.assertEquals("Database is still modified", database.isModified(), false);

		IDatabase database2 = Kwery.getDatabase(folder);
		ITable tableC2 = database2.getTable("c", false);
		String value2 = tableC2.getAll()[0].getValue("s");

		Assert.assertEquals("Entity value mismatch", "cc", value2);
	}
}
