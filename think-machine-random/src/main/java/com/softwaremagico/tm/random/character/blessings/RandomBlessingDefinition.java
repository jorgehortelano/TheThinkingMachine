package com.softwaremagico.tm.random.character.blessings;

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

import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.blessings.Blessing;
import com.softwaremagico.tm.rules.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.BlessingClassification;
import com.softwaremagico.tm.rules.character.blessings.BlessingFactory;
import com.softwaremagico.tm.rules.character.blessings.BlessingGroup;
import com.softwaremagico.tm.rules.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.rules.character.skills.AvailableSkill;
import com.softwaremagico.tm.rules.log.RandomGenerationLog;

public class RandomBlessingDefinition extends RandomSelector<Blessing> {

	public RandomBlessingDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		final IGaussianDistribution blessingDistribution = BlessingNumberPreferences.getSelected(getPreferences());
		// Select a blessing
		final int totalSelectedBlessings = blessingDistribution.randomGaussian();
		while (getCharacterPlayer().getBlessings().size() < totalSelectedBlessings
				+ getCharacterPlayer().getFaction().getBlessings(BlessingClassification.BLESSING).size()) {
			final Blessing selectedBlessing = selectElementByWeight();
			try {
				getCharacterPlayer().addBlessing(selectedBlessing);
				RandomGenerationLog.info(this.getClass().getName(), "Added blessing '" + selectedBlessing + "'.");
				removeElementWeight(selectedBlessing);
			} catch (TooManyBlessingsException e) {
				// No more possible.
				break;
			} catch (BlessingAlreadyAddedException e) {
				removeElementWeight(selectedBlessing);
				continue;
			}
		}
	}

	@Override
	protected Collection<Blessing> getAllElements() throws InvalidXmlElementException {
		return BlessingFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(Blessing blessing) throws InvalidRandomElementSelectedException {
		if (blessing == null) {
			return 0;
		}
		if (blessing.getBlessingGroup() == BlessingGroup.RESTRICTED) {
			throw new InvalidRandomElementSelectedException("Blessing '" + blessing + "' is restricted.");
		}
		// Only curses.
		if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
			throw new InvalidRandomElementSelectedException("Blessing '" + blessing + "' is a curse.");
		}
		// If specialization is set, add blessings that affects the skills with
		// ranks.
		final SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
		for (final AvailableSkill skill : blessing.getAffectedSkill(getCharacterPlayer().getLanguage())) {
			// More specialized, less ranks required to skip the curse.
			if (specializationPreferences.mean() >= SpecializationPreferences.FAIR.mean()) {
				if (getCharacterPlayer().getSkillAssignedRanks(skill) >= specializationPreferences.mean()) {
					return GOOD_PROBABILITY;
				}
			}
		}
		for (final AvailableSkill skill : blessing.getAffectedSkill(getCharacterPlayer().getLanguage())) {
			// More specialized, less ranks required to skip the curse.
			if (getCharacterPlayer().getSkillAssignedRanks(skill) >= (10 - specializationPreferences.maximum())) {
				return FAIR_PROBABILITY;
			}
		}
		return 1;
	}

	@Override
	protected void assignIfMandatory(Blessing blessing) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		final BlessingPreferences blessingPreferences = BlessingPreferences.getSelected(getPreferences());
		if (blessingPreferences != null && blessing.getBlessingGroup() == BlessingGroup.get(blessingPreferences.name())) {
			try {
				getCharacterPlayer().addBlessing(blessing);
				RandomGenerationLog.info(this.getClass().getName(), "Added blessing '" + blessing + "'.");
			} catch (TooManyBlessingsException | BlessingAlreadyAddedException e) {
				throw new ImpossibleToAssignMandatoryElementException("Impossible to assign mandatory blessing '" + blessing + "'.", e);
			}
		}
	}

	@Override
	protected void assignMandatoryValues(Set<Blessing> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}