package com.softwaremagico.tm.random.profiles;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.json.ExcludeFromJson;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomProfile extends Element<RandomProfile> implements IRandomProfile {
	private final Set<IRandomPreference> randomPreferences;
	private final Map<CharacteristicName, Integer> characteristicsMinimumValues;
	@ExcludeFromJson
	public boolean parentMerged = false;

	public RandomProfile(String id, String name, String language, Set<IRandomPreference> randomPreferences,
			Map<CharacteristicName, Integer> characteristicsMinimumValues) {
		super(id, name, language);
		this.randomPreferences = randomPreferences;
		this.characteristicsMinimumValues = characteristicsMinimumValues;
	}

	@Override
	public void setParent(IRandomProfile randomProfile) {
		if (!parentMerged) {
			// Merge preferences.
			Set<IRandomPreference> combinedPreferences = mergePreferences(randomProfile.getPreferences(), randomPreferences);
			randomPreferences.clear();
			randomPreferences.addAll(combinedPreferences);

			// Merge Characteristics
			Map<CharacteristicName, Integer> characteristicsMinimumValues = randomProfile.getCharacteristicsMinimumValues();
			for (Entry<CharacteristicName, Integer> entry : getCharacteristicsMinimumValues().entrySet()) {
				characteristicsMinimumValues.put(entry.getKey(), entry.getValue());
			}
			this.characteristicsMinimumValues.clear();
			this.characteristicsMinimumValues.putAll(characteristicsMinimumValues);

			parentMerged = true;
		}
	}

	protected static Set<IRandomPreference> removeAny(Set<IRandomPreference> originalPreferences, IRandomPreference preferenceToRemove) {
		for (IRandomPreference randomPreference : new HashSet<>(originalPreferences)) {
			if (randomPreference.getClass().equals(preferenceToRemove.getClass())) {
				originalPreferences.remove(randomPreference);
			}
		}
		return originalPreferences;
	}

	protected static Set<IRandomPreference> mergePreferences(Set<IRandomPreference> originalPreferences, Set<IRandomPreference> preferredPreferences) {
		for (IRandomPreference preferredPreference : preferredPreferences) {
			originalPreferences = removeAny(originalPreferences, preferredPreference);
		}
		originalPreferences.addAll(preferredPreferences);
		return originalPreferences;
	}

	@Override
	public Map<CharacteristicName, Integer> getCharacteristicsMinimumValues() {
		return characteristicsMinimumValues;
	}

	@Override
	public Map<AvailableSkill, Integer> getSkillsMinimumValues() {
		return new HashMap<>();
	}

	@Override
	public Set<Blessing> getBlessings() {
		return new HashSet<>();
	}

	@Override
	public Set<AvailableBenefice> getBenefices() {
		return new HashSet<>();
	}

	@Override
	public int getExperiencePoints() {
		return 0;
	}

	@Override
	public Set<IRandomPreference> getPreferences() {
		return randomPreferences;
	}

	public boolean isParentMerged() {
		return parentMerged;
	}

	public void setParentMerged(boolean parentMerged) {
		this.parentMerged = parentMerged;
	}
}
