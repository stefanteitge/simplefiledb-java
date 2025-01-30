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
import de.stefanteitge.simplefiledb.DatabaseException;
import de.stefanteitge.simplefiledb.IEntity;
import de.stefanteitge.simplefiledb.ITable;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class Entity implements IEntity {

  private final Table table;

  private final Map<String, String> fields;

  private boolean modified;

  public Entity(Table table, Map<String, String> fields) {
    this.table = table;
    this.fields = fields;
  }

  @Override
  public <T> T beanify(Class<T> clazz) throws DatabaseException {
    try {
      T bean = clazz.getConstructor().newInstance();

      for (String column : getTable().getColumns()) {
        PropertyUtils.setSimpleProperty(bean, column, getValue(column));
      }

      return bean;
    } catch (InstantiationException e) {
      throw new DatabaseException("Beanify failed", e);
    } catch (IllegalAccessException e) {
      throw new DatabaseException("Beanify failed", e);
    } catch (NoSuchMethodException e) {
      throw new DatabaseException("Beanify failed", e);
    } catch (InvocationTargetException e) {
      throw new DatabaseException("Beanify failed", e);
    }
  }

  @Override
  public ITable getTable() {
    return table;
  }

  @Override
  public String getValue(String column) {
    if (!isColumnInTable(column)) {
      if (getTable().getDatabase().getConfig().getRequireColumnDeclaration()) {
        // TODO maybe use custom runtime exception here
        throw new RuntimeException("Column " + column + " does no exist in table");
      }

      return null;
    }

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
      if (getTable().getDatabase().getConfig().getRequireColumnDeclaration()) {
        // TODO maybe use custom runtime exception here
        throw new RuntimeException("Column " + column + " does no exist in table");
      }

      getTable().addColumn(column);
    }

    String oldValue = fields.put(column, value);

    if ((oldValue != null && !oldValue.equals(value)) || (oldValue == null && value != null)) {
      modified = true;
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass())
        .add("Table", table)
        .toString();
  }

  protected boolean isColumnInTable(String column) {
    return Arrays.asList(table.getColumns()).contains(column);
  }
}
