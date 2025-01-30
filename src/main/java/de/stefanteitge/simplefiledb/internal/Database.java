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

package de.stefanteitge.simplefiledb.internal;

import com.google.common.base.MoreObjects;
import de.stefanteitge.simplefiledb.DatabaseConfig;
import de.stefanteitge.simplefiledb.DatabaseException;
import de.stefanteitge.simplefiledb.IDatabase;
import de.stefanteitge.simplefiledb.ITable;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class Database implements IDatabase {

  public static final String EXTENSION = ".simplefiledb";

  private Map<String, Table> tableMap;

  private final File directory;

  private DatabaseConfig config;

  /**
   * Initialized the database abstraction.
   * @param directory the physical directory containing the tables.
   * @param config the configuration use.
   */
  public Database(File directory, DatabaseConfig config) {
    this.directory = directory;
    this.config = config;

    tableMap = new HashMap<String, Table>();

    File[] tableFiles = directory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name != null && name.endsWith(EXTENSION);
      }
    });

    for (File tableFile : tableFiles) {
      try {
        Table table = new Table(this, tableFile);
        tableMap.put(table.getName(), table);
      } catch (DatabaseException e) {
        // ignore
        // TODO log
      }
    }
  }

  public DatabaseConfig getConfig() {
    return config;
  }

  public File getDirectory() {
    return directory;
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
      File tableFile = new File(directory, name + EXTENSION);

      try {
        table = new Table(this, tableFile);
        tableMap.put(name, table);
      } catch (DatabaseException e) {
        return null;
      }
    }

    return table;
  }

  @Override
  public void flush() throws DatabaseException {
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
    return MoreObjects.toStringHelper(getClass())
        .add("Path", directory.getAbsolutePath())
        .add("Table count", tableMap.size())
        .toString();
  }
}
