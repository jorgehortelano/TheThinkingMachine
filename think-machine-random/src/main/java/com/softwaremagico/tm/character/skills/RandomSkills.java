package com.softwaremagico.tm.character.skills;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTraitCategory;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomSkills extends RandomSelector<AvailableSkill> {
	private List<Entry<CharacteristicType, Integer>> preferredCharacteristicsTypeSorted;

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		this(characterPlayer, preferences, new HashSet<AvailableSkill>(), new HashSet<AvailableSkill>());
	}

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<AvailableSkill> requiredSkills,
			Set<AvailableSkill> suggestedSkills) throws InvalidXmlElementException {
		super(characterPlayer, null, preferences, requiredSkills, suggestedSkills);
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

		// Meanwhile are ranks to expend.
		while (getCharacterPlayer().getSkillsTotalPoints() < FreeStyleCharacterCreation.getSkillsPoints(getCharacterPlayer().getInfo().getAge())
				+ difficultLevel.getSkillsBonus()) {
			// Select a skill randomly.
			AvailableSkill selectedSkill = selectElementByWeight();

			// Assign random ranks to the skill.
			assignRandomRanks(selectedSkill);

			// Remove skill from options to avoid adding more ranks.
			removeElementWeight(selectedSkill);
		}
	}

	@Override
	protected void assignMandatoryValues(Set<AvailableSkill> mandatoryValues) throws InvalidXmlElementException {
		DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

		for (AvailableSkill requiredSkill : mandatoryValues) {
			if (getCharacterPlayer().getSkillsTotalPoints() < FreeStyleCharacterCreation.getSkillsPoints(getCharacterPlayer().getInfo().getAge())
					+ difficultLevel.getSkillsBonus()) {
				assignRandomRanks(requiredSkill);
			}
		}
	}

	@Override
	protected void assignIfMandatory(AvailableSkill skill) throws InvalidXmlElementException {
		// Set skills to use equipment.
		Weapon weapon = getCharacterPlayer().hasWeaponWithSkill(skill);
		if (weapon != null) {
			// Assign random ranks to the skill.
			int ranksAssigned = assignRandomRanks(skill);
			RandomGenerationLog.debug(this.getClass().getName(), "Assigning '" + ranksAssigned + "' ranks for '" + skill + "' needed for a selected weapon '"
					+ weapon + "'.");
			// Remove skill from options to avoid adding more ranks.
			removeElementWeight(skill);
		}
		// If selected skill has some ranks added by preferences, must have at
		// least the minimum.
		if (getCharacterPlayer().isSkillTrained(skill)) {
			SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
			int skillRanks = getCharacterPlayer().getSkillTotalRanks(skill);
			if (skillRanks < selectedSpecialization.minimum()) {
				// Assign random ranks to the skill.
				assignRandomRanks(skill);
			}
		}
		// Some Cybernetics needs skills
		for (SelectedCyberneticDevice cyberneticDevice : getCharacterPlayer().getCybernetics()) {
			CyberneticDeviceTrait usability = cyberneticDevice.getTrait(CyberneticDeviceTraitCategory.USABILITY);
			if (usability != null && usability.getId().equalsIgnoreCase("skillUse")) {
				if (skill.getId().equalsIgnoreCase(cyberneticDevice.getCyberneticDevice().getId())) {
					// Assign random ranks to the skill.
					assignRandomRanks(skill);
				}
			}
		}
	}

	public void mergeSkills(AvailableSkill availableSkill, SkillGroup skillGroup) throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		int weight = getWeight(availableSkill);
		while (weight > 0) {
			for (AvailableSkill selectedSkill : AvailableSkillsFactory.getInstance().getSkillsByGroup(skillGroup, getCharacterPlayer().getLanguage())) {
				if (weight > 0 && !Objects.equals(availableSkill, selectedSkill)) {
					updateWeight(selectedSkill, getWeight(selectedSkill) + 1);
					weight--;
				}
			}
		}
		removeElementWeight(availableSkill);
	}

	@Override
	protected Collection<AvailableSkill> getAllElements() throws InvalidXmlElementException {
		Set<AvailableSkill> availableSkills = new HashSet<AvailableSkill>();
		for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getAvailableSkills(skillDefinition, getCharacterPlayer().getLanguage())) {
				availableSkills.add(skill);
			}
		}
		return availableSkills;
	}

	@Override
	protected int getWeight(AvailableSkill skill) throws InvalidRandomElementSelectedException {
		int weight = 1;

		if (skill.getSkillDefinition().isNatural()) {
			RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' as natural skill is increased.");
			weight += 3;
		}

		int characteristicsWeight = weightByCharacteristics(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by characteristics modification is '" + characteristicsWeight + "'.");
		weight += characteristicsWeight;

		int preferencesWeight = weightByPreferences(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by preferences modification is '" + preferencesWeight + "'.");
		weight += preferencesWeight;

		int factionWeight = weightByFactions(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by faction modification is '" + factionWeight + "'.");
		weight += factionWeight;

		int nobilityWeight = weightByNobility(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by nobility modification is '" + nobilityWeight + "'.");
		weight += nobilityWeight;

		int specializationWeight = weightBySpecializationPreferences(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by specialization modification is '" + specializationWeight + "'.");
		weight += specializationWeight;

		int psiqueWeight = weightByPsique(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by psique modification is '" + psiqueWeight + "'.");
		weight += psiqueWeight;

		int combatWeight = weightByCombat(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by combat definitions is '" + combatWeight + "'.");
		weight += combatWeight;

		int specializationMultiplier = weightBySpecializationSize(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Specialization multiplier for '" + skill + "' is '" + specializationMultiplier + "'.");
		return weight * specializationMultiplier;
	}

	private int weightByCharacteristics(AvailableSkill skill) throws InvalidRandomElementSelectedException {
		if (skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups() != null && !getPreferredCharacteristicsTypeSorted().isEmpty()) {
			if (Objects.equals(skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups(), getPreferredCharacteristicsTypeSorted().get(0))) {
				return FAIR_PROBABILITY;
			}
			if (Objects.equals(skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups(), getPreferredCharacteristicsTypeSorted().get(1))) {
				return LITTLE_PROBABILITY;
			}
		}
		return 0;
	}

	/**
	 * Skills with lots of specializations has more chance to have at least one
	 * of the specializations selected. This methods reduces its probability.
	 * 
	 * @param skill
	 *            skill to check.
	 * @return multiplier for the other bonus.
	 */
	private int weightBySpecializationSize(AvailableSkill skill) {
		// Skills with lots of specializations has more probability to get one
		// of them that other skills. Reduce this probability.
		if (skill.getSkillDefinition().getSpecializations() == null || skill.getSkillDefinition().getSpecializations().isEmpty()) {
			return AvailableSkillsFactory.getInstance().getMaximumNumberOfSpecializations();
		}
		return AvailableSkillsFactory.getInstance().getMaximumNumberOfSpecializations() / skill.getSkillDefinition().getSpecializations().size();
	}

	private int weightByFactions(AvailableSkill skill) throws InvalidRandomElementSelectedException {
		// No faction skills
		if (skill.getSkillDefinition().isLimitedToFaction()) {
			if (!skill.getSkillDefinition().getFactions().contains(getCharacterPlayer().getFaction())) {
				throw new InvalidRandomElementSelectedException("Skill '" + skill + "' restricted to factions '" + skill.getSkillDefinition().getFactions()
						+ "'.");
			} else if (getCharacterPlayer().getFaction() != null
			// Recommended to my faction and only this faction can do it.
					&& skill.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getFaction())) {
				return MAX_PROBABILITY;
			}
		}

		return 0;
	}

	private int weightByNobility(AvailableSkill skill) throws InvalidRandomElementSelectedException {
		if (getCharacterPlayer().getFaction() != null && Objects.equals(getCharacterPlayer().getFaction().getFactionGroup(), FactionGroup.NOBILITY)) {
			// beastcraft for nobility is not common in my point of view.
			if (skill.getId().equalsIgnoreCase("beastcraft")) {
				throw new InvalidRandomElementSelectedException("Skill '" + skill + "' not desiderable for faction '" + getCharacterPlayer().getFaction()
						+ "'.");
			}
		}
		return 0;
	}

	private int weightByPreferences(AvailableSkill skill) {
		// Specialization by selection.
		if (getPreferences().contains(SkillGroupPreferences.getSkillGroupPreference(skill.getSkillDefinition().getSkillGroup()))) {
			int skillRanks = getCharacterPlayer().getSkillTotalRanks(skill);

			// Good probability for values between the specialization.
			if (skillRanks < SkillGroupPreferences.getSkillGroupPreference(skill.getSkillDefinition().getSkillGroup()).maximum()) {
				return MAX_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightBySpecializationPreferences(AvailableSkill skill) throws InvalidRandomElementSelectedException {
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		int skillRanks = getCharacterPlayer().getSkillTotalRanks(skill);
		// No more that the maximum allowed.
		if (skillRanks > selectedSpecialization.maximum()) {
			throw new InvalidRandomElementSelectedException("Skill '" + skill + "' has the maximum ranks preferred.");
		}

		// Good probability for values between the specialization.
		if (skillRanks > selectedSpecialization.minimum()) {
			return GOOD_PROBABILITY;
		}
		return 0;
	}

	private int weightByPsique(AvailableSkill skill) {
		if (getCharacterPlayer().getPsiqueLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage())) > 0) {
			// Self control useful for psique.
			if (skill.getId().equals("selfControl")) {
				return MAX_PROBABILITY;
			}
		}
		if (getCharacterPlayer().getPsiqueLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage())) > 0) {
			if (skill.getId().equals("influence")) {
				return MAX_PROBABILITY;
			}
		}
		return 0;
	}

	public List<Entry<CharacteristicType, Integer>> getPreferredCharacteristicsTypeSorted() {
		if (preferredCharacteristicsTypeSorted == null) {
			preferredCharacteristicsTypeSorted = getCharacterPlayer().getPreferredCharacteristicsTypeSorted();
		}
		return preferredCharacteristicsTypeSorted;
	}

	private int assignRandomRanks(AvailableSkill availableSkill) throws InvalidXmlElementException {
		int finalRanks = getRankValue(availableSkill);
		if (finalRanks < 0) {
			finalRanks = 0;
		}
		// Only if adding more ranks.
		if (finalRanks < getCharacterPlayer().getSkillAssignedRanks(availableSkill)) {
			return 0;
		}
		// If specializations allows it.
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		if (getCharacterPlayer().getSkillAssignedRanks(availableSkill) >= selectedSpecialization.maximum()) {
			return 0;
		}

		finalRanks = checkMaxSkillRanksValues(availableSkill, finalRanks);

		getCharacterPlayer().setSkillRank(availableSkill, finalRanks);
		return finalRanks;
	}

	protected int checkMaxSkillRanksValues(AvailableSkill availableSkill, int finalRanks) throws InvalidXmlElementException {
		// If respects age maximum.
		if (finalRanks > FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge())) {
			finalRanks = FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge());
		}

		DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

		// Final ranks cannot be greater that the total points remaining.
		if (getCharacterPlayer().getSkillsTotalPoints() + (finalRanks - getCharacterPlayer().getSkillAssignedRanks(availableSkill)) > FreeStyleCharacterCreation
				.getSkillsPoints(getCharacterPlayer().getInfo().getAge()) + difficultLevel.getSkillsBonus()) {
			finalRanks = (FreeStyleCharacterCreation.getSkillsPoints(getCharacterPlayer().getInfo().getAge()) + difficultLevel.getSkillsBonus())
					- getCharacterPlayer().getSkillsTotalPoints();
		}
		return finalRanks;
	}

	protected int getRankValue(AvailableSkill availableSkill) throws InvalidXmlElementException {
		int finalSkillValue = 0;
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		int minimumValue = selectedSpecialization.minimum();
		// Natural skills always a minimum value of 3.
		if (availableSkill.getSkillDefinition().isNatural()
				&& minimumValue < FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getCharacterPlayer().getInfo().getAge())) {
			minimumValue = FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getCharacterPlayer().getInfo().getAge());
		}
		// Gaussian distribution.
		finalSkillValue = selectedSpecialization.randomGaussian();

		// Update combat due to difficulties.
		if (availableSkill.getSkillDefinition().getSkillGroup().equals(SkillGroup.COMBAT)) {
			DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());
			switch (difficultLevel) {
			case VERY_EASY:
				if (finalSkillValue > selectedSpecialization.minimum()) {
					finalSkillValue--;
				}
			case VERY_HARD:
				if (finalSkillValue < selectedSpecialization.maximum()) {
					finalSkillValue++;
				}
				break;
			default:
				break;
			}
		}

		if (finalSkillValue < minimumValue) {
			finalSkillValue = minimumValue;
		}

		// Cannot be less that the actual value.
		if (finalSkillValue < getCharacterPlayer().getSkillAssignedRanks(availableSkill)) {
			return getCharacterPlayer().getSkillAssignedRanks(availableSkill);
		}

		// Not more than the max allowed.
		if (finalSkillValue > FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge())) {
			finalSkillValue = FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge());
		}
		return finalSkillValue;
	}

	/**
	 * Combat skills must be interesting for fighters.
	 * 
	 * @param availableSkill
	 * @return
	 */
	private int weightByCombat(AvailableSkill availableSkill) {
		CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
		// Set some attack skills
		if (combatPreferences.minimum() >= CombatPreferences.FAIR.minimum()) {
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 5) {
				if (Objects.equals(availableSkill.getId(), "energyGuns")) {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			} else if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 3) {
				if (Objects.equals(availableSkill.getId(), "slugGuns")) {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			} else {
				if (Objects.equals(availableSkill.getId(), "archery")) {
					return FAIR_PROBABILITY * combatPreferences.maximum();
				}
			}
			if (Objects.equals(availableSkill.getId(), "fight")) {
				if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 3) {
					return FAIR_PROBABILITY * combatPreferences.maximum();
				} else {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			}
		}
		return 0;
	}

}
