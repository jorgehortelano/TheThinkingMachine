package com.softwaremagico.tm.rules.character;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import java.util.List;
import java.util.Map.Entry;

import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.characteristics.Characteristic;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.rules.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.rules.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.rules.character.equipment.armours.Armour;
import com.softwaremagico.tm.rules.character.equipment.shields.Shield;
import com.softwaremagico.tm.rules.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.rules.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.rules.character.occultism.OccultismPath;
import com.softwaremagico.tm.rules.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.rules.character.skills.AvailableSkill;
import com.softwaremagico.tm.rules.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.rules.log.MachineLog;

public class ThreatLevel {
	private static final int DAMAGE_THREAT_MULTIPLICATOR = 2;
	private static final int DAMAGE_AREA_THREAT_MULTIPLICATOR = 3;
	private static final int DAMAGE_TYPES_THREAT_MULTIPLICATOR = 3;
	private static final int COMBAT_STYLES_THREAT_MULTIPLICATOR = 5;
	private static final int EXTRA_WYRD_THREAT_MULTIPLICATOR = 3;
	private static final int PSI_LEVEL_THREAT_MULTIPLICATOR = 2;
	private static final int VITALITY_THREAT_MULTIPLICATOR = 2;

	private static int total = 0;
	private static int combatThreatLevel = 0;
	private static int weaponThreatLevel = 0;
	private static int armourThreatLevel = 0;
	private static float shieldThreatLevel = 0;
	private static int cyberneticsThreatLevel = 0;
	private static int occultismThreatLevel = 0;
	private static int vitalityThreatLevel = 0;
	private static float totalThreatLevel = 0;
	private static float totalMoney = 0;

	public static int getThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		threatLevel += getCombatThreatLevel(characterPlayer);
		threatLevel += getThreatLevel(characterPlayer.getArmour());
		threatLevel += getVitalityThreatLevel(characterPlayer);
		threatLevel += getThreatLevel(characterPlayer.getCybernetics());
		threatLevel += getOccultismThreatLevel(characterPlayer);
		threatLevel *= getThreatLevelMultiplicator(characterPlayer.getShield());
		totalThreatLevel += threatLevel;
		totalMoney += characterPlayer.getInitialMoney();
		total++;
		return threatLevel;
	}

	private static int getThreatLevel(CharacterPlayer characterPlayer, Characteristic characteristic,
			AvailableSkill skill) {
		return characterPlayer.getSkillTotalRanks(skill) + characteristic.getValue();
	}

	private static int getCombatThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		if (!characterPlayer.getAllWeapons().isEmpty()) {
			final Weapon mainWeapon = characterPlayer.getMainWeapon();
			threatLevel += getThreatLevel(characterPlayer,
					characterPlayer.getCharacteristic(mainWeapon.getCharacteristic().getCharacteristicName()),
					mainWeapon.getSkill()) * 2;
			threatLevel += getThreatLevel(mainWeapon);
			if (WeaponType.getMeleeTypes().contains(mainWeapon.getType())) {
				threatLevel += characterPlayer.getStrengthDamangeModification() * DAMAGE_THREAT_MULTIPLICATOR;
			}
			threatLevel += characterPlayer.getAllWeapons().size() - 1;
		}
		threatLevel += characterPlayer.getMeleeCombatStyles().size() * COMBAT_STYLES_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getRangedCombatStyles().size() * COMBAT_STYLES_THREAT_MULTIPLICATOR;
		threatLevel += getThreatLevel(characterPlayer, characterPlayer.getCharacteristic(CharacteristicName.DEXTERITY),
				AvailableSkillsFactory.getInstance().getElement("fight", characterPlayer.getLanguage()));
		combatThreatLevel += threatLevel;
		return threatLevel;
	}

	private static int getThreatLevel(Weapon weapon) {
		if (weapon == null) {
			return 0;
		}
		int threatLevel = 0;
		threatLevel += weapon.getMainDamage() * DAMAGE_THREAT_MULTIPLICATOR;
		threatLevel += weapon.getAreaMeters() * DAMAGE_AREA_THREAT_MULTIPLICATOR;
		threatLevel += weapon.getDamageTypes().size() * DAMAGE_TYPES_THREAT_MULTIPLICATOR;
		try {
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("fire", weapon.getLanguage()))) {
				threatLevel += 3 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("laser", weapon.getLanguage()))) {
				threatLevel += 1 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("xaser", weapon.getLanguage()))) {
				threatLevel += 3 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("graser", weapon.getLanguage()))) {
				threatLevel += 6 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("shock", weapon.getLanguage()))) {
				threatLevel += 5 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("plasma", weapon.getLanguage()))) {
				threatLevel += 5 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("halveArmour", weapon.getLanguage()))) {
				threatLevel += 3 * weapon.getMainDamage();
			}
			if (weapon.getDamageTypes().contains(
					DamageTypeFactory.getInstance().getElement("ignoreArmour", weapon.getLanguage()))) {
				threatLevel += 6 * weapon.getMainDamage();
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
		}
		threatLevel += weapon.getMainRange() / 10;
		threatLevel += weapon.getMainRate();
		// threatLevel += weapon.getAccesories().size();
		if (weapon.isAutomaticWeapon()) {
			threatLevel *= 2;
		}
		weaponThreatLevel += threatLevel;
		return threatLevel;
	}

	private static int getThreatLevel(Armour armour) {
		if (armour == null) {
			return 0;
		}
		int threatLevel = 0;
		threatLevel += armour.getProtection() * DAMAGE_THREAT_MULTIPLICATOR;
		threatLevel += armour.getDamageTypes().size() * armour.getProtection();
		armourThreatLevel += threatLevel;
		return threatLevel;
	}

	private static int getThreatLevel(List<SelectedCyberneticDevice> cyberneticDevices) {
		int threatLevel = 0;
		for (final SelectedCyberneticDevice cyberneticDevice : cyberneticDevices) {
			switch (cyberneticDevice.getCyberneticDevice().getClassification()) {
			case COMBAT:
				threatLevel += cyberneticDevice.getPoints() * 2;
				break;
			case ENHANCEMENT:
			case ALTERATION:
				threatLevel += cyberneticDevice.getPoints() / 2;
				break;
			case OTHERS:
				break;
			}
		}
		cyberneticsThreatLevel += threatLevel;
		return threatLevel;
	}

	private static int getOccultismThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		for (final Entry<String, List<String>> occulstismPathEntry : characterPlayer.getSelectedPowers().entrySet()) {
			 final OccultismPath occultismPath = OccultismPathFactory.getInstance().getElement(occulstismPathEntry.getKey(),
					characterPlayer.getLanguage());
			for (final String occultismPowerName : occulstismPathEntry.getValue()) {
				switch (occultismPath.getClassification()) {
				case COMBAT:
					threatLevel += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel();
					break;
				case ENHANCEMENT:
				case ALTERATION:
				case OTHERS:
					threatLevel += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel() / 2;
					break;
				}
			}
		}
		threatLevel += characterPlayer.getExtraWyrd() * EXTRA_WYRD_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getPsi(characterPlayer.getLanguage()))
				* PSI_LEVEL_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage()))
				* PSI_LEVEL_THREAT_MULTIPLICATOR;
		occultismThreatLevel += threatLevel;
		return threatLevel;
	}

	private static float getThreatLevelMultiplicator(Shield shield) {
		if (shield == null) {
			return 1;
		}
		float threatLevel = 0;
		threatLevel += ((shield.getForce() - (double) shield.getImpact()) + 5) / 10;
		threatLevel += ((float) shield.getImpact()) / 10;
		shieldThreatLevel += threatLevel;
		return threatLevel;
	}

	private static int getVitalityThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		final int threatLevel = characterPlayer.getVitalityValue() * VITALITY_THREAT_MULTIPLICATOR;
		vitalityThreatLevel += threatLevel;
		return threatLevel;
	}

	public static void showStaticis() {
		if (total > 0) {
			System.out.println("######################################");
			System.out.println("Total threat: " + totalThreatLevel / total + " (" + total + ")");
			System.out.println("Combat threat: " + combatThreatLevel / total);
			System.out.println("Weapon threat: " + weaponThreatLevel / total);
			System.out.println("Armour threat: " + armourThreatLevel / total);
			System.out.println("Shield threat: " + shieldThreatLevel / total);
			System.out.println("Cybernetics threat: " + cyberneticsThreatLevel / total);
			System.out.println("Occultism threat: " + occultismThreatLevel / total);
			System.out.println("Vitality threat: " + vitalityThreatLevel / total);
			System.out.println("Money: " + totalMoney / total);
		}
	}

	public static void resetStatistics() {
		total = 0;
		combatThreatLevel = 0;
		weaponThreatLevel = 0;
		armourThreatLevel = 0;
		shieldThreatLevel = 0;
		cyberneticsThreatLevel = 0;
		occultismThreatLevel = 0;
		vitalityThreatLevel = 0;
		totalThreatLevel = 0;
	}
}