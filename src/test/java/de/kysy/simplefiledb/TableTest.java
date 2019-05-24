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
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TableTest {
	
	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void testGetColums() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));

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
	public void testColumnDeclarationRequired() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		database.getConfig().setRequireColumnDeclaration(false);
		ITable table = database.getTable("b", false);
		IEntity entity = table.simpleQuery("r", "b");
		entity.setValue("new", "newValue");
		
		Assert.assertEquals("Column count didn't rise", 5, table.getColumns().length);
		
		String value = entity.getValue("newnew");

		Assert.assertNull("Column must be retrievable", value);
		
		IDatabase database2 = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		database2.getConfig().setRequireColumnDeclaration(true);
		ITable table2 = database2.getTable("b", false);
		IEntity entity2 = table2.simpleQuery("r", "b");
		
		boolean hasException = false;
		try {
			entity2.setValue("new2", "newValue2");
		} catch (RuntimeException e) {
			hasException = true;
		}
		
		Assert.assertEquals("Column count rose", 4, table2.getColumns().length);
		Assert.assertEquals("Column access must result in exception", true, hasException);
		
		boolean hasException2 = false;
		try {
			value = entity2.getValue("newnew");
		} catch (Exception e) {
			hasException2 = true;
		}

		Assert.assertEquals("Column access must result in exception", true, hasException2);
	}

	@Test
	public void testGetDatabase() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", false);

		Assert.assertNotNull("Table may not be null", table);

		Assert.assertNotNull("Database in table may not be null", table.getDatabase());
	}

	@Test
	public void testGetAll() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("b", false);

		Assert.assertNotNull("Table may not be null", table);

		String[] columns = table.getColumns();

		Assert.assertEquals("Column count mismatch", 4, columns.length);

		IEntity[] entities = table.getAll();

		Assert.assertEquals("Entity count mismatch", 2, entities.length);
	}

	@Test
	public void testSimpleQuery() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);

		IEntity entity = table.simpleQuery("r", "b");

		Assert.assertNotNull("Entity my not be null", entity);

		Assert.assertEquals("Column value mismatch", "d", entity.getValue("t"));
	}

	@Test(expected = RuntimeException.class)
	public void testSimpleQueryInvalidColumn() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);

		table.simpleQuery("z", "b");
	}

	@Test
	public void testCreateEntity() throws DatabaseException {
		IDatabase database = DatabaseFactory.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("c", false);

		int entityCount = table.getAll().length;

		IEntity entity = table.createEntity();

		entity.setValue("t", "new");

		int entityCount2 = table.getAll().length;

		Assert.assertEquals("Entity count mismatch", entityCount + 1, entityCount2);
	}

	@Test
	public void testCreateTable() throws DatabaseException, IOException {
		File folder = temporaryFolder.newFolder();

		File tableFile = new File(folder, "a.simplefiledb");

		tableFile.delete();

		Assert.assertTrue("Table file must be deleted at this point", !tableFile.exists());

		IDatabase database = DatabaseFactory.getDatabase(folder);

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", true);
		table.ensureColumnsExist(new String[] {"b"});

		IEntity entity = table.createEntity();
		entity.setValue("b", "c");

		table.flush();

		Assert.assertTrue("Table file must exist at this point", tableFile.exists());

		Assert.assertTrue("Table file must have a size greater zero", tableFile.length() > 0);
	}

	@Test
	public void testCreateTableDisallowed() throws DatabaseException, IOException {
		File folder = temporaryFolder.newFolder();

		File tableFile = new File(folder, "a.simplefiledb");

		tableFile.delete();

		Assert.assertTrue("Table file must be deleted at this point", !tableFile.exists());

		IDatabase database = DatabaseFactory.getDatabase(folder);

		Assert.assertNotNull("Database may not be null", database);

		ITable table = database.getTable("a", false);

		Assert.assertNull("Table must be null", table);
	}
}
