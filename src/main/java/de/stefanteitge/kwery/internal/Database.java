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

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

import de.stefanteitge.kwery.IDatabase;
import de.stefanteitge.kwery.ITable;
import de.stefanteitge.kwery.KweryConfig;
import de.stefanteitge.kwery.KweryException;

public class Database implements IDatabase {

	public static final String KWERY_EXTENSION = ".kwery";

	private Map<String, Table> tableMap;

	private final File directory;

	private KweryConfig config;

	public File getDirectory() {
		return directory;
	}

	public Database(File directory, KweryConfig config) {
		this.directory = directory;
		this.config = config;

		tableMap = new HashMap<String, Table>();

		File[] tableFiles = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name != null && name.endsWith(KWERY_EXTENSION);
			}
		});

		for (File tableFile : tableFiles) {
			try {
				Table table = new Table(this, tableFile);
				tableMap.put(table.getName(), table);
			} catch (KweryException e) {
				// ignore
				// TODO log
			}
		}
	}

	public KweryConfig getConfig() {
		return config;
	}

	@Override
	public ITable[] getTables() {
		return tableMap.values().toArray(new ITable[0]);
	}

	@Override
	public ITable getTable(String name, boolean create) {
		// TODO check name [A-Za-z]+[A-Za-z0-9]*

		Table table = tableMap.get(name);

		if (table == null && create) {
			File tableFile = new File(directory, name + KWERY_EXTENSION);

			try {
				table = new Table(this, tableFile);
				tableMap.put(name, table);
			} catch (KweryException e) {
				return null;
			}
		}

		return table;
	}

	@Override
	public void flush() throws KweryException {
		for (Table table : tableMap.values()) {
			if (table.isModified()) {
				table.flush();
			}
		}
	}

	@Override
	public boolean isModified() {
		for (Table table : tableMap.values()) {
			if (table.isModified()) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(getClass())
			      .add("Path", directory.getAbsolutePath())
			      .add("Table count", tableMap.size())
			      .toString();
	}
}
