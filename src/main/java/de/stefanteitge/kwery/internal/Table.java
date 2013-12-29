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
package de.stefanteitge.kwery.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Objects;

import de.stefanteitge.kwery.IDatabase;
import de.stefanteitge.kwery.IEntity;
import de.stefanteitge.kwery.ITable;
import de.stefanteitge.kwery.KweryException;

public class Table implements ITable {

	private final File file;

	private List<Entity> entityList;

	private String[] columns;

	private String name;

	private final IDatabase database;

	private boolean entityCountModified;
	
	private boolean columnsModified;

	public Table(Database database, File file) throws KweryException {
		this.database = database;
		this.file = file;
		columns = new String[0];

		// TODO query create boolean flag
		// TODO: touch file if it does not exist
		if (!file.exists()){
			  try {
				file.createNewFile();
			} catch (IOException e) {
				throw new KweryException("Could not create table file", e);
			}
		}

		name = file.getName().replaceAll(Database.KWERY_EXTENSION, "");

		entityList = new ArrayList<Entity>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			boolean first = true;
			String s;
			while ((s = br.readLine()) != null) {
				String[] fields = s.split(Pattern.quote(getDatabase().getConfig().getFieldSeparator()));

				if (first) {
					columns = fields;
					first = false;
				} else {
					entityList.add(new Entity(this, createEntityMap(fields)));
				}
			}
			fr.close();
		} catch (FileNotFoundException e) {
			throw new KweryException("Table file not found", e);
		} catch (IOException e) {
			throw new KweryException("Table file access failed", e);
		}
	}

	@Override
	public void addColumn(String column) {
		// TODO Auto-generated method stub
		columns = KweryUtil.concat(columns, new String[] {column});
		columnsModified = true;
	}

	@Override
	public IEntity createEntity() {
		Entity entity = new Entity(this, new HashMap<String, String>());

		entityList.add(entity);
		entityCountModified = true;

		return entity;
	}

	@Override
	public void ensureColumnsExist(String[] newColumns) {
		for (String newColumn : newColumns) {
			if (!isColumnInTable(newColumn)) {
				addColumn(newColumn);
			}
		}
	}

	@Override
	public IEntity[] getAll() {
		return entityList.toArray(new IEntity[0]);
	}

	@Override
	public IDatabase getDatabase() {
		return database;
	}

	@Override
	public String[] getColumns() {
		return columns;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IEntity simpleQuery(String column, String value) {
		if (!isColumnInTable(column)) {
			if (getDatabase().getConfig().getRequireColumnDeclaration()) {
				// TODO maybe use custom runtime exception here
				throw new RuntimeException("Column " + column + " does no exist in table");
			}
			
			addColumn(column);
		}

		for (Entity entity : entityList) {
			String queryValue = entity.getValue(column);

			boolean areBothNull = queryValue == null && value == null;
			boolean comparisonByValue = value != null && value.equals(queryValue);
			boolean comparisonByQValue = queryValue != null && queryValue.equals(value);
			if (areBothNull || comparisonByValue || comparisonByQValue) {
				return entity;
			}
		}

		return null;
	}

	@Override
	public void flush() throws KweryException {
		try {
			FileWriter writer = new FileWriter(file);
			PrintWriter bw = new PrintWriter(writer);

			bw.println(makeColumnRow());

			for (IEntity entity : entityList) {
				bw.println(makeRow(entity));
				entity.setModified(false);
			}

			bw.close();

			entityCountModified = false;
			columnsModified = false;
		} catch (IOException e) {
			// TODO rollback on failure (isModified)?
			throw new KweryException("Failed to save table " + name, e);
		}
	}

	@Override
	public boolean isModified() {
		if (columnsModified) {
			return true;
		}
		
		if (entityCountModified) {
			return true;
		}

		for (Entity entity : entityList) {
			if (entity.isModified()) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
			      .add("Name", name)
			      .toString();
	}

	protected boolean isColumnInTable(String column) {
		// TODO duplicate method in Entity
		return Arrays.asList(columns).contains(column);
	}

	private Map<String, String> createEntityMap(String[] fields) {
		int length = Math.min(columns.length, fields.length);

		if (fields.length > length) {
			// TODO log too many column data here
		}

		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < length; i++) {
			map.put(columns[i], fields[i]);
		}

		return map;
	}

	private String makeRow(IEntity entity) {
		String row = "";

		for (int i = 0; i < columns.length; i++) {
			String field = entity.getValue(columns[i]);
			if (field == null) {
				field = "";
			}

			if (i == 0) {
				row = field;
			} else {
				row += getDatabase().getConfig().getFieldSeparator() + field;
			}
		}

		return row;
	}

	private String makeColumnRow() {
		String row = "";

		for (int i = 0; i < columns.length; i++) {
			if (i == 0) {
				row = columns[i];
			} else {
				row += getDatabase().getConfig().getFieldSeparator() + columns[i];
			}
		}

		return row;
	}
}
