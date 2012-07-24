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

import java.util.Arrays;
import java.util.Map;

import de.stefanteitge.kwery.IEntity;
import de.stefanteitge.kwery.ITable;

public class Entity implements IEntity {

	private final Table table;

	private final Map<String, String> fields;

	private boolean modified;

	public Entity(Table table, Map<String, String> fields) {
		this.table = table;
		this.fields = fields;
	}

	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public String getValue(String column) {
		return fields.get(column);
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	public void setValue(String column, String value) {
		if (!isColumnInTable(column)) {
			// TODO maybe use custom runtime exception here
			throw new RuntimeException("Column " + column + " does no exist in table");
		}

		String oldValue = fields.put(column, value);

		if (oldValue != null && !oldValue.equals(value)) {
			modified = true;
		}
	}

	protected boolean isColumnInTable(String column) {
		return Arrays.asList(table.getColumns()).contains(column);
	}
}
