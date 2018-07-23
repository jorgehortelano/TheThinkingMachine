package com.softwaremagico.tm.random.selectors;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public enum CombatPreferences implements IRandomPreferences {
	PEACEFUL(0, 1),

	FAIR(2, 5),

	BELLIGERENT(4, 10);

	private final int minimum;
	private final int maximum;

	private CombatPreferences(int minimum, int maximum) {
		this.maximum = maximum;
		this.minimum = minimum;
	}

	@Override
	public int maximum() {
		return maximum;
	}

	@Override
	public int minimum() {
		return minimum;
	}
}
