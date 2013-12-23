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

import de.stefanteitge.kwery.internal.Database;

public class Kwery {

	private Kwery() {
	}

	public static IDatabase getDatabase(File directory) throws KweryException {
		return getDatabase(directory, KweryConfig.createDefault());
	}
	
	public static IDatabase getDatabase(File directory, KweryConfig config) throws KweryException {
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			throw new KweryException("Database directory is invalid");
		}

		return new Database(directory, config);
	}
}
