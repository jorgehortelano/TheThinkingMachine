package com.softwaremagico.tm.tests.factory;

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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.blessings.BlessingFactory;

@Test(groups = { "blessingFactory" })
public class BlessingFactoryTests {
	private static final String LANGUAGE = "es";
	private static final int DEFINED_BLESSINGS = 97;
	private static final int DEFINED_BONIFICATIONS_MISSING_EYE = 2;

	@Test
	public void readBlessings() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BLESSINGS, BlessingFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void multiplesBonifications() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BONIFICATIONS_MISSING_EYE,
				BlessingFactory.getInstance().getElement("missingEye", LANGUAGE).getBonifications().size());
	}
}