package com.softwaremagico.tm.character;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;
import com.softwaremagico.tm.log.MachineLog;

public class CostCalculator {

	public static int getCost(CharacterPlayer characterPlayer) {
		int cost = 0;
		MachineLog.info(CostCalculator.class.getName(), "################## " + characterPlayer.getInfo().getName());
		if (characterPlayer.getRace() != null) {
			cost += characterPlayer.getRace().getCost();
			MachineLog.info(CostCalculator.class.getName(), "Race cost: " + characterPlayer.getRace().getCost());
		}
		cost += getCharacteristicsCost(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Characteristics cost: " + getCharacteristicsCost(characterPlayer));
		cost += getSkillCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Skills cost: " + getSkillCosts(characterPlayer));
		cost += getTraitsCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Traits cost: " + getTraitsCosts(characterPlayer));
		cost += getPsiPowersCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Psi powers cost: " + getPsiPowersCosts(characterPlayer));
		cost += getCyberneticsCost(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Cybernetics cost: " + getCyberneticsCost(characterPlayer));
		MachineLog.info(CostCalculator.class.getName(), "Total cost: " + cost);
		return cost;
	}

	private static int getCharacteristicsCost(CharacterPlayer characterPlayer) {
		return (characterPlayer.getCharacteristicsTotalPoints() - FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) * 3;
	}

	private static int getSkillCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (AvailableSkill skill : SkillFactory.getNaturalSkills(characterPlayer.getLanguage())) {
			cost += characterPlayer.getSkillValue(skill) - 3;
		}
		for (AvailableSkill skill : SkillFactory.getLearnedSkills(characterPlayer.getLanguage())) {
			if (characterPlayer.isSkillSpecial(skill)) {
				continue;
			}
			if (characterPlayer.getSkillValue(skill) != null) {
				cost += characterPlayer.getSkillValue(skill);
			}
		}
		return (cost - FreeStyleCharacterCreation.SKILLS_POINTS);
	}

	private static int getTraitsCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		cost += getBlessingCosts(characterPlayer);
		cost += getBenefitsCosts(characterPlayer);
		return cost - FreeStyleCharacterCreation.TRAITS_POINTS;
	}

	private static int getBlessingCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Blessing blessing : characterPlayer.getBlessings()) {
			cost += blessing.getCost();
		}
		return cost;
	}

	private static int getBenefitsCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Benefit benefit : characterPlayer.getBenefits()) {
			cost += benefit.getCost();
		}
		for (Benefit affliction : characterPlayer.getAfflictions()) {
			cost += affliction.getCost();
		}
		return cost;
	}

	private static int getPsiPowersCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (OccultismPower occultismPower : characterPlayer.getOccultism().getElements()) {
			cost += occultismPower.getLevel();
		}
		cost += characterPlayer.getOccultism().getExtraWyrd() * 2;
		cost += (characterPlayer.getOccultism().getPsiValue() - characterPlayer.getRace().getPsi()) * 3;
		cost += (characterPlayer.getOccultism().getTeurgyValue() - characterPlayer.getRace().getTeurgy()) * 3;
		return cost;
	}

	private static int getCyberneticsCost(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Device device : characterPlayer.getCybernetics().getElements()) {
			cost += device.getPoints();
		}
		return cost;
	}
}
