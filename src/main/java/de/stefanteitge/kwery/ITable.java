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

public interface ITable {

	void addColumn(String column);

	IEntity createEntity();

	void ensureColumnsExist(String[] strings);

	void flush() throws KweryException;

	IEntity[] getAll();

	IDatabase getDatabase();

	String[] getColumns();

	String getName();

	boolean isModified();

	IEntity simpleQuery(String column, String value);
}
