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

package de.stefanteitge.simplefiledb;

import de.stefanteitge.simplefiledb.internal.Database;

import java.io.File;

public class DatabaseFactory {

  private DatabaseFactory() {
  }

  public static IDatabase getDatabase(File directory) throws DatabaseException {
    return getDatabase(directory, DatabaseConfig.createDefault());
  }

  public static IDatabase getDatabase(File directory, DatabaseConfig config) throws DatabaseException {
    if (directory == null || !directory.exists() || !directory.isDirectory()) {
      throw new DatabaseException("Database directory is invalid");
    }

    return new Database(directory, config);
  }
}
