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

import org.junit.Assert;
import org.junit.Test;

public class EntityTest {

	@Test
	public void testGetValue() throws KweryException {
		IDatabase database = Kwery.getDatabase(new File(TestSettings.TEST01_PATH));
		ITable table = database.getTable("b", false);
		IEntity[] entities = table.getAll();

		String valueR = entities[1].getValue("r");

		Assert.assertEquals("Entity value mismatch", "f", valueR);

		boolean hadException = false;
		try {
			entities[0].getValue("z");
		} catch (RuntimeException e) {
			hadException = true;
		}

		Assert.assertEquals("Entity not null", true, hadException);
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

	@Test(expected = RuntimeException.class)
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

		boolean isModified = entity.getTable().getDatabase().isModified();
		Assert.assertEquals("Table is modified", isModified, false);

		entity.setValue("r", "f");

		Assert.assertEquals("Entity is modified", entity.isModified(), false);

		entity.setValue("r", "ff");

		Assert.assertEquals("Entity is not modified", entity.isModified(), true);

		Assert.assertEquals("Table is not modified", entity.getTable().isModified(), true);

		boolean isModified2 = entity.getTable().getDatabase().isModified();
		Assert.assertEquals("Table is not modified", isModified2, true);
	}
}
