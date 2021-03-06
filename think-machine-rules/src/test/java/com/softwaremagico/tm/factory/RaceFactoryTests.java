package com.softwaremagico.tm.factory;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"raceFactory"})
public class RaceFactoryTests {
    private static final String LANGUAGE = "en";
    private static final int DEFINED_RACES = 10;
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        Assert.assertEquals((int) RaceFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        Assert.assertEquals((int) RaceFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_RACES);
    }

    @Test
    public void readRaces() throws InvalidXmlElementException {
        Assert.assertEquals(RaceFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .size(), DEFINED_RACES);
    }


    @Test
    public void readAfflictions() throws InvalidXmlElementException {
        final Race vorox = RaceFactory.getInstance().getElement("vorox", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertTrue(vorox.getBenefices().contains(AvailableBeneficeFactory.getInstance().getElement("noOccult",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

    @Test
    public void readMoreAfflictions() throws InvalidXmlElementException {
        final Race hironem = RaceFactory.getInstance().getElement("hironem", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertTrue(hironem.getBenefices().contains(AvailableBeneficeFactory.getInstance().getElement("noPsi",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

    @Test
    public void readRaceExtraPoints() throws InvalidXmlElementException {
        final Race vorox = RaceFactory.getInstance().getElement("vorox", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(vorox.getCost(), 9);
    }

    @Test
    public void readPlanets() throws InvalidXmlElementException {
        Assert.assertEquals(RaceFactory.getInstance().getElement("shantor", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getPlanets().size(), 1);
    }
}
