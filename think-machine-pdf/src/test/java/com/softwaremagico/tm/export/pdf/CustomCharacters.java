package com.softwaremagico.tm.export.pdf;

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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.OccultismType;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicImprovement;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.CyberneticSkill;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

@Test(groups = { "customCharacterGeneration" })
public class CustomCharacters {
	private final static String LANGUAGE = "en";

	@Test
	public void createPaolaCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setName("#5");
		player.getInfo().setPlayer("Paola");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(25);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet("Ligaheim");
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(8);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("appliedScience", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physicalScience", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("empathy", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("thinkMachine", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 7);

		player.addBlessing(BlessingFactory.getInstance().getElement("innovative", LANGUAGE));
		player.addBlessing(BlessingFactory.getInstance().getElement("haughty", LANGUAGE));

		player.addBlessing(BlessingFactory.getInstance().getElement("hacker", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("greaseMonkey", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("beastFoe", player.getLanguage()));

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [turingLanguage]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [apprentice]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("gossipNetwork_3", player.getLanguage()));

		Device ingeneerEye = new Device("Ojo de Ingeniero", 8, 6, "Normal", "Normal", "Automático", "Oculto", "Autoalimentado");
		ingeneerEye.addCharacteristicImprovement(new CharacteristicImprovement(CharacteristicsDefinitionFactory.getInstance()
				.getElement("perception", LANGUAGE), 1, false));
		player.getCybernetics().addElement(ingeneerEye);

		Device secondBrain = new Device("Segundo Cerebro", 11, 10, "Normal", "Normal", "Automático", "Oculto", "Autoalimentado");
		secondBrain.addCharacteristicImprovement(new CharacteristicImprovement(CharacteristicsDefinitionFactory.getInstance().getElement("wits", LANGUAGE), 2,
				true));
		secondBrain.addSkillImprovement(new CyberneticSkill(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE), 4, true));
		secondBrain.addSkillImprovement(new CyberneticSkill(AvailableSkillsFactory.getInstance().getElement("lore", "energyPistolsLore", LANGUAGE), 4, true));
		secondBrain.addSkillImprovement(new CyberneticSkill(AvailableSkillsFactory.getInstance().getElement("lore", "thinkMachineLore", LANGUAGE), 4, true));
		player.getCybernetics().addElement(secondBrain);

		player.setShield(new Shield("De Duelo", 5, 10, 15));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
	}

	@Test
	public void characterAnaCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setName("Ana");
		player.getInfo().setPlayer("Ana");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(22);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet("Leminkainen");
		player.setFaction(FactionsFactory.getInstance().getElement("hawkwood", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(5);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(4);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("arts", "drawing", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("streetWise", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("watercraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("artifactMelee", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("leadership", LANGUAGE), 2);

		player.addBlessing(BlessingFactory.getInstance().getElement("unyielding", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("prideful", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("nobility [knight]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [latinLanguage]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("imperialCharter", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("fluxSword", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("vendetta_2", player.getLanguage()));

		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("fluxSword", LANGUAGE));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
	}

	@Test
	public void createCarlosCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setName("Carlos");
		player.getInfo().setPlayer("Carlos");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(28);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet("Byzantium Sec.");
		player.setFaction(FactionsFactory.getInstance().getElement("scravers", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(7);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(8);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(6);
		player.getCharacteristic(CharacteristicName.WITS).setValue(6);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(4);
		player.getCharacteristic(CharacteristicName.WILL).setValue(6);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("streetWise", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("aircraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("demolitions", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("leadership", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("survival", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("torture", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("theMan", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("possessive", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [entered]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("passageContracts_3", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", player.getLanguage()));

		CombatStyle fightStyle = new CombatStyle("ironHeel");
		fightStyle.addElement(new CombatAction("Cadena de Destrucción", null, "3d", "Presa Especial"));
		fightStyle.addElement(new CombatAction("Cabezazo", 2, "4d", "Ignora armadura*"));
		player.getMeleeCombatStyles().add(fightStyle);

		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("shotgunSolid", LANGUAGE));
		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("mediumAutofeedHandgun", LANGUAGE));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
		Assert.assertEquals(player.getRank(), "Genin");
	}

	@Test
	public void createNoeliaCharacer() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setName("Noelia");
		player.getInfo().setPlayer("Noelia");
		player.getInfo().setGender(Gender.FEMALE);
		// player.getInfo().setAge(30);
		player.setRace(RaceFactory.getInstance().getElement("obun", LANGUAGE));
		player.getInfo().setPlanet("Obun");
		player.setFaction(FactionsFactory.getInstance().getElement("obun", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(6);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(4);
		player.getCharacteristic(CharacteristicName.TECH).setValue(3);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(8);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 5);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("empathy", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "kelantiLore", LANGUAGE), 3);

		player.getOccultism().setPsiValue(6);

		player.getOccultism().addElement(new OccultismPower("Mano Levitante", OccultismType.PSI, "Vol+Autoc.", 1, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Lanzadora", OccultismType.PSI, "Vol+Autoc.", 2, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Aplastante", OccultismType.PSI, "Vol+Autoc.", 3, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Duelista", OccultismType.PSI, "Vol+Autoc.", 4, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Nº5", OccultismType.PSI, "", 5, "", "", "", 1, false));
		player.getOccultism().addElement(new OccultismPower("Intuir", OccultismType.PSI, "Vol+Empatía", 1, "Toque", "Instantáneo", "", 1));
		player.getOccultism().addElement(new OccultismPower("Emocionar", OccultismType.PSI, "Pre+Influenciar", 2, "Toque", "Instantáneo", "", 1));
		player.getOccultism().addElement(new OccultismPower("Visión Mental", OccultismType.PSI, "Vol+Empatía", 3, "Toque", "Instantáneo", "", 1));
		player.getOccultism().setExtraWyrd(3);

		player.addBlessing(BlessingFactory.getInstance().getElement("just", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("condescending", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("ordained [novitiate]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [urthish]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_2", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("orphan", player.getLanguage()));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
	}

	@Test
	public void createGolemCharacer() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setPlayer("PNJ");
		player.getInfo().setName("A (Prototipo A)");
		player.getInfo().setGender(Gender.FEMALE);
		player.setRace(new Race("Gólem", 5, 5, 5, 3, 3, 6, 0, 0, 0, 6, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet("Ligaheim");
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", LANGUAGE));
		player.getInfo().setAge(1432);

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(12);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(10);
		player.getCharacteristic(CharacteristicName.WITS).setValue(5);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("throwing", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("atheltics", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "thinkMachineLore", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("thinkMachine", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("gullible", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("righteous", player.getLanguage()));

		player.getCybernetics().addElement(new Device("Omnienchufe", 1, 0, "Normal", "Normal", "Automático", "Oculto", ""));
		player.getCybernetics().addElement(new Device("Interfaz de Datos (Turing)", 1, 1, "Normal", "Normal", "Automático", "Oculto", "Turing"));
		player.getCybernetics().addElement(new Device("Armadura", 1, 2, "Normal", "Normal", "Automático", "Oculto", "2d"));

		player.setArmour(new Armour("Piel", 2, false, false, false, false, false, false, false, 5, 0, 0, 0, 0));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Golem.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), -2);
	}
}
