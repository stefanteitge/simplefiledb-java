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

public class KweryConfig {

	public static final String DEFAULT_FIELD_SEPARATOR = "||";
	
	private String fieldSeparator;
	
	private boolean requireColumnDeclaration;
	
	private KweryConfig() {
	}
	
	public static KweryConfig createDefault() {
		KweryConfig config = new KweryConfig();
		config.setFieldSeparator(DEFAULT_FIELD_SEPARATOR);
		config.setRequireColumnDeclaration(true);
		return config;
	}

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public boolean getRequireColumnDeclaration() {
		return requireColumnDeclaration;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	
	public void setRequireColumnDeclaration(boolean requireColumnDeclaration) {
		this.requireColumnDeclaration = requireColumnDeclaration;
	}
}
