package de.stefanteitge.kwery;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.stefanteitge.kwery.IDatabase;
import de.stefanteitge.kwery.IEntity;
import de.stefanteitge.kwery.ITable;
import de.stefanteitge.kwery.Kwery;
import de.stefanteitge.kwery.KweryException;

public class EntityTest {

	@Test
	public void testGetValue() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);
		IEntity[] entities = table.getAll();

		String valueR = entities[1].getValue("r");

		Assert.assertEquals("Entity value mismatch", "f", valueR);

		String valueZ = entities[0].getValue("z");

		Assert.assertNull("Entity not null", valueZ);
	}

	@Test
	public void testSetValue() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);
		IEntity[] entities = table.getAll();

		entities[1].setValue("r", "ff");

		String valueR = entities[1].getValue("r");

		Assert.assertEquals("Entity value mismatch", "ff", valueR);
	}

	@Test(expected=RuntimeException.class)
	public void testSetValueInvalidColumn() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);
		IEntity[] entities = table.getAll();

		entities[1].setValue("z", "ff");
	}

	@Test
	public void testIsModifed() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);
		IEntity[] entities = table.getAll();

		IEntity entity = entities[1];

		Assert.assertEquals("Entity is modified", entity.isModified(), false);

		Assert.assertEquals("Table is modified", entity.getTable().isModified(), false);

		Assert.assertEquals("Table is modified", entity.getTable().getDatabase().isModified(), false);

		entity.setValue("r", "f");

		Assert.assertEquals("Entity is modified", entity.isModified(), false);

		entity.setValue("r", "ff");

		Assert.assertEquals("Entity is not modified", entity.isModified(), true);

		Assert.assertEquals("Table is not modified", entity.getTable().isModified(), true);

		Assert.assertEquals("Table is not modified", entity.getTable().getDatabase().isModified(), true);
	}
}
