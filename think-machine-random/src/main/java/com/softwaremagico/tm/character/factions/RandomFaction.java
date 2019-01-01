package com.softwaremagico.tm.character.factions;

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

import java.util.Collection;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomFaction extends RandomSelector<Faction> {

	public RandomFaction(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().setFaction(selectElementByWeight());
	}

	@Override
	protected Collection<Faction> getAllElements() throws InvalidXmlElementException {
		return FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(Faction faction) {
		// Specialization desired.
		FactionPreferences selectedFactionGroup = FactionPreferences.getSelected(getPreferences());
		if (selectedFactionGroup != null) {
			if (faction.getFactionGroup() != null && faction.getFactionGroup().name().equalsIgnoreCase(selectedFactionGroup.name())) {
				return 1;
			}
			// Different faction than selected.
			return 0;
		}
		// Humans only humans factions.
		if (faction.getRestrictedRace() != null && !faction.getRestrictedRace().equals(getCharacterPlayer().getRace())) {
			return 0;
		}
		// No faction preference selected. All factions has the same
		// probability.
		return 1;
	}

	@Override
	protected void assignIfMandatory(Faction element) throws InvalidXmlElementException {
		return;
	}
}
