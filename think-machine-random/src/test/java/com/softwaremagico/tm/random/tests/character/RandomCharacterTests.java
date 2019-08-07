package com.softwaremagico.tm.random.tests.character;

import java.util.HashSet;

/*-
 * #%L
 * Think Machine (Core)
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

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.character.benefices.RandomBeneficeDefinition;
import com.softwaremagico.tm.random.character.equipment.weapons.RandomRangeWeapon;
import com.softwaremagico.tm.random.character.equipment.weapons.RandomWeapon;
import com.softwaremagico.tm.random.character.skills.RandomSkills;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticTotalDevicesPreferences;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.NamesPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.PsiquePathLevelPreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.rules.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.rules.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.BlessingClassification;
import com.softwaremagico.tm.rules.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.rules.character.characteristics.Characteristic;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.rules.character.creation.CostCalculator;
import com.softwaremagico.tm.rules.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.rules.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.rules.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.rules.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.rules.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.rules.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.rules.character.factions.FactionGroup;
import com.softwaremagico.tm.rules.character.factions.FactionsFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismType;
import com.softwaremagico.tm.rules.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.rules.character.races.RaceFactory;
import com.softwaremagico.tm.rules.character.skills.AvailableSkill;
import com.softwaremagico.tm.rules.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.rules.character.skills.SkillDefinition;
import com.softwaremagico.tm.rules.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.rules.language.LanguagePool;
import com.softwaremagico.tm.rules.log.MachineLog;
import com.softwaremagico.tm.rules.txt.CharacterSheet;

@Test(groups = { "randomCharacter" })
public class RandomCharacterTests {
	private static final String LANGUAGE = "es";

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test(expectedExceptions = { DuplicatedPreferenceException.class })
	public void preferencesCollision()
			throws InvalidXmlElementException, DuplicatedPreferenceException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		new RandomizeCharacter(characterPlayer, 0, TechnologicalPreferences.MEDIEVAL,
				TechnologicalPreferences.FUTURIST);
	}

	@Test
	public void chooseRaceAndFactionTest() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.HUMAN,
				FactionPreferences.NOBILITY);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.NOBILITY);
		Assert.assertEquals(characterPlayer.getRace(),
				RaceFactory.getInstance().getElement(RacePreferences.HUMAN.name(), LANGUAGE));
	}

	@Test
	public void chooseRaceAndFactionTestXeno() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.OBUN,
				FactionPreferences.GUILD);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.GUILD);
		Assert.assertEquals(characterPlayer.getRace(),
				RaceFactory.getInstance().getElement(RacePreferences.OBUN.name(), LANGUAGE));
	}

	@Test
	public void readRandomSkillConfigurationarchery() throws InvalidXmlElementException, DuplicatedPreferenceException {
		final SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("archery", "en");
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 0);
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 2);
	}

	@Test
	public void checkWeightLimitedByDefinition()
			throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomWeapon randomWeapons = new RandomRangeWeapon(characterPlayer, null, new HashSet<Weapon>());
		final Weapon largeRock = WeaponFactory.getInstance().getElement("veryLargeRock", LANGUAGE);
		Assert.assertEquals(randomWeapons.getTotalWeight(largeRock), 0);
	}

	@Test(expectedExceptions = { InvalidRandomElementSelectedException.class })
	public void checkSkillLimitationByTechnology()
			throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(7);

		final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
		final AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement("archery", LANGUAGE);
		randomSkills.validateElement(availableSkill);
	}

	@Test(expectedExceptions = { InvalidRandomElementSelectedException.class })
	public void checkSkillLimitationByLowTechnology()
			throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(1);

		final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
		final AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE);
		randomSkills.validateElement(availableSkill);
	}

	@Test(expectedExceptions = { InvalidRandomElementSelectedException.class })
	public void checkBeneficeLimitationByRace()
			throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));

		final RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, null);
		final AvailableBenefice benefice = AvailableBeneficeFactory.getInstance().getElement("language [urthish]",
				LANGUAGE);
		randomBenefice.validateElement(benefice.getRandomDefinition());
	}

	@Test
	public void readRandomSkillConfigurationSlugs() throws InvalidXmlElementException, DuplicatedPreferenceException {
		final SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("slugGuns", "en");
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 2);
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 6);
	}

	@Test
	public void selectSkillGroup() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				SkillGroupPreferences.COMBAT);
		randomizeCharacter.createCharacter();

		Assert.assertEquals(CostCalculator.getCost(characterPlayer),
				FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
		Assert.assertTrue(characterPlayer.getRanksAssigned(SkillGroupPreferences.COMBAT.getSkillGroup()) > 10);
	}

	@Test
	public void mustHaveStatus() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		characterPlayer.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				StatusPreferences.HIGHT);
		randomizeCharacter.createCharacter();
		Assert.assertNotNull(characterPlayer.getRank());
		Assert.assertEquals(CostCalculator.getCost(characterPlayer),
				FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
	}

	@Test
	public void checkBlessingPreferences() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				CurseNumberPreferences.FAIR, BlessingNumberPreferences.HIGH);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		randomizeCharacter.createCharacter();
		try {
			Assert.assertTrue(characterPlayer.getCurses().size() >= CurseNumberPreferences.FAIR.minimum()
					+ characterPlayer.getFaction().getBlessings(BlessingClassification.CURSE).size());
			Assert.assertTrue(characterPlayer.getCurses().size() <= CurseNumberPreferences.FAIR.maximum()
					+ characterPlayer.getFaction().getBlessings(BlessingClassification.CURSE).size());
		} catch (Error ae) {
			final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			System.out.println(characterSheet.toString());
			throw ae;
		}

		try {
			Assert.assertTrue(characterPlayer.getAllBlessings().size() >= BlessingNumberPreferences.HIGH.minimum()
					+ characterPlayer.getFaction().getBlessings().size());
			Assert.assertTrue(characterPlayer.getAllBlessings().size() <= BlessingNumberPreferences.HIGH.maximum()
					+ characterPlayer.getFaction().getBlessings().size());
		} catch (Error ae) {
			final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			System.out.println(characterSheet.toString());
			throw ae;
		}
	}

	@Test
	public void createPsiqueCharacter() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				SpecializationPreferences.SPECIALIZED, PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH,
				StatusPreferences.FAIR);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getSelectedPowers().values().size() > 0);
	}

	@Test
	public void createChurchCharacter() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		characterPlayer.setFaction(FactionsFactory.getInstance().getElement("orthodox", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				SpecializationPreferences.SPECIALIZED, PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH,
				StatusPreferences.FAIR);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getTotalSelectedPowers() > 0);
	}

	@Test
	public void voroxCannotHavePsique() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				SpecializationPreferences.SPECIALIZED, PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getFaction().getBenefices()
				.contains(AvailableBeneficeFactory.getInstance().getElement("noOccult", LANGUAGE)));
		Assert.assertTrue(characterPlayer.getAfflictions()
				.contains(AvailableBeneficeFactory.getInstance().getElement("noOccult", LANGUAGE)));
		for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(LANGUAGE)) {
			Assert.assertEquals(characterPlayer.getPsiqueLevel(occultismType), 0);
		}
		Assert.assertEquals(characterPlayer.getTotalSelectedPowers(), 0);
	}

	@Test
	public void namesByStatus() throws InvalidXmlElementException, InvalidRandomElementSelectedException,
			DuplicatedPreferenceException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				NamesPreferences.VERY_HIGHT);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getInfo().getNames().size() >= 2);
		Assert.assertTrue(characterPlayer.getInfo().getSurname() != null);
	}

	@Test
	public void weapons() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException, BeneficeAlreadyAddedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE), 5);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE), 5);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE), 5);
		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(7);
		characterPlayer
				.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds2000]", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				CombatPreferences.BELLIGERENT);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getAllWeapons().size() >= 2);
	}

	@Test
	public void age() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				AgePreferences.PREADOLESCENT);
		randomizeCharacter.createCharacter();
		final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		System.out.println(characterSheet.toString());

		Assert.assertEquals(FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(CharacteristicName.DEXTERITY,
				characterPlayer.getInfo().getAge(), characterPlayer.getRace()), 4);
		Assert.assertEquals(FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()),
				4);
		for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.BODY)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation
					.getMaxInitialCharacteristicsValues(characteristic.getCharacteristicName(),
							characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}
		for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.MIND)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation
					.getMaxInitialCharacteristicsValues(characteristic.getCharacteristicName(),
							characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}
		for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.SPIRIT)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation
					.getMaxInitialCharacteristicsValues(characteristic.getCharacteristicName(),
							characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}

		for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(LANGUAGE)) {
			try {
				Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation
						.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
			} catch (AssertionError e) {
				MachineLog.severe(this.getClass().getName(), "Invalid skill ranks in " + skill + " ("
						+ characterPlayer.getSkillAssignedRanks(skill) + "). Max allowed: "
						+ FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
				throw e;
			}
		}
		for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(LANGUAGE)) {
			try {
				Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation
						.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
			} catch (AssertionError e) {
				MachineLog.severe(this.getClass().getName(), "Invalid skill ranks in " + skill + " ("
						+ characterPlayer.getSkillAssignedRanks(skill) + "). Max allowed: "
						+ FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
				throw e;
			}
		}
	}

	@Test
	public void weaponsSkills() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.addWeapon(WeaponFactory.getInstance().getElement("axe", LANGUAGE));
		characterPlayer.addWeapon(WeaponFactory.getInstance().getElement("martechGold", LANGUAGE));

		Assert.assertNotNull(
				characterPlayer.hasWeaponWithSkill(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE)));
		Assert.assertNotNull(characterPlayer
				.hasWeaponWithSkill(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE)));

		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(6);

		final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
		final AvailableSkill energyGuns = AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE);
		randomSkills.validateElement(energyGuns.getRandomDefinition());
		final AvailableSkill fight = AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE);
		randomSkills.validateElement(fight.getRandomDefinition());

		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		Assert.assertTrue(characterPlayer
				.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE)) > 0);
		Assert.assertTrue(characterPlayer
				.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE)) > 0);
	}

	@Test
	public void cybernetics() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
				CyberneticTotalDevicesPreferences.CYBORG, CyberneticPointsPreferences.SOUL_LESS);
		randomizeCharacter.createCharacter();

		Assert.assertTrue(
				characterPlayer.getCybernetics().size() >= CyberneticTotalDevicesPreferences.CYBORG.minimum());
	}

	@Test
	public void cyberneticsSkills()
			throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.getCharacteristic(CharacteristicName.WILL).setValue(6);
		characterPlayer.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("spyEye", LANGUAGE));
		characterPlayer.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("etherEar", LANGUAGE));
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		Assert.assertTrue(characterPlayer
				.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("spyEye", LANGUAGE)) > 0);
		Assert.assertTrue(characterPlayer
				.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("etherEar", LANGUAGE)) > 0);
	}

}