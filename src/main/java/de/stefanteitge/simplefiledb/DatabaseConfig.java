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

public class DatabaseConfig {

  private static final String DEFAULT_ENCODING = "UTF-8";

  public static final String DEFAULT_FIELD_SEPARATOR = "||";

  private String fieldSeparator;

  private boolean requireColumnDeclaration;

  private String encoding;

  private DatabaseConfig() {
  }

  public static DatabaseConfig createDefault() {
    DatabaseConfig config = new DatabaseConfig();
    config.setEncoding(DEFAULT_ENCODING);
    config.setFieldSeparator(DEFAULT_FIELD_SEPARATOR);
    config.setRequireColumnDeclaration(true);
    return config;
  }

  public String getEncoding() {
    return encoding;
  }


  public String getFieldSeparator() {
    return fieldSeparator;
  }

  public boolean getRequireColumnDeclaration() {
    return requireColumnDeclaration;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public void setFieldSeparator(String fieldSeparator) {
    this.fieldSeparator = fieldSeparator;
  }

  public void setRequireColumnDeclaration(boolean requireColumnDeclaration) {
    this.requireColumnDeclaration = requireColumnDeclaration;
  }
}
