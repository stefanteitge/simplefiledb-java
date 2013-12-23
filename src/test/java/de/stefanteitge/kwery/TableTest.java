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

import de.stefanteitge.kwery.IDatabase;
import de.stefanteitge.kwery.IEntity;
import de.stefanteitge.kwery.ITable;
import de.stefanteitge.kwery.Kwery;
import de.stefanteitge.kwery.KweryException;

public class TableTest {
	
	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void testGetColums() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", false);

		Assert.assertNotNull("Table may not be null", table);

		String[] columns = table.getColumns();

		Assert.assertEquals("Column count mismatch", 4, columns.length);

		Assert.assertEquals("Column name mismatch", "m", columns[0]);

		Assert.assertEquals("Column name mismatch", "n", columns[1]);

		Assert.assertEquals("Column name mismatch", "o", columns[2]);

		Assert.assertEquals("Column name mismatch", "p", columns[3]);
	}

	@Test
	public void testGetDatabase() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", false);

		Assert.assertNotNull("Table may not be null", table);

		Assert.assertNotNull("Database in table may not be null", table.getDatabase());
	}

	@Test
	public void testGetAll() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("b", false);

		Assert.assertNotNull("Table may not be null", table);

		String[] columns = table.getColumns();

		Assert.assertEquals("Column count mismatch", 4, columns.length);

		IEntity[] entities = table.getAll();

		Assert.assertEquals("Entity count mismatch", 2, entities.length);
	}

	@Test
	public void testSimpleQuery() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);

		IEntity entity = table.simpleQuery("r", "b");

		Assert.assertNotNull("Entity my not be null", entity);

		Assert.assertEquals("Column value mismatch", "d", entity.getValue("t"));
	}

	@Test(expected=RuntimeException.class)
	public void testSimpleQueryInvalidColumn() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);

		table.simpleQuery("z", "b");
	}

	@Test
	public void testCreateEntity() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("c", false);

		int entityCount = table.getAll().length;

		IEntity entity = table.createEntity();

		entity.setValue("t", "new");

		int entityCount2 = table.getAll().length;

		Assert.assertEquals("Entity count mismatch", entityCount + 1, entityCount2);
	}

	@Test
	public void testCreateTable() throws KweryException, IOException {
		File folder = temporaryFolder.newFolder();

		File tableFile = new File(folder, "a.kwery");

		tableFile.delete();

		Assert.assertTrue("Table file must be deleted at this point", !tableFile.exists());

		IDatabase database = Kwery.getDatabase(folder);

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", true);
		table.ensureColumnsExist(new String[] {"b"});

		IEntity entity = table.createEntity();
		entity.setValue("b", "c");

		table.flush();

		Assert.assertTrue("Table file must exist at this point", tableFile.exists());

		Assert.assertTrue("Table file must have a size greate zero", tableFile.length() > 0);
	}

	@Test
	public void testCreateTableDisallowed() throws KweryException, IOException {
		File folder = temporaryFolder.newFolder();

		File tableFile = new File(folder, "a.kwery");

		tableFile.delete();

		Assert.assertTrue("Table file must be deleted at this point", !tableFile.exists());

		IDatabase database = Kwery.getDatabase(folder);

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", false);

		Assert.assertNull("Table must be null", table);
	}
}
