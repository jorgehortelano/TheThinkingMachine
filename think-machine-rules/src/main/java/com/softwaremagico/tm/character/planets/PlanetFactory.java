package com.softwaremagico.tm.character.planets;

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

import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.language.ITranslator;

public class PlanetFactory extends XmlFactory<Planet> {
	private static final String TRANSLATOR_FILE = "planets.xml";

	private static final String NAME = "name";
	private static final String FACTION = "factions";

	private static class PlanetFactoryInit {
		public static final PlanetFactory INSTANCE = new PlanetFactory();
	}

	public static PlanetFactory getInstance() {
		return PlanetFactoryInit.INSTANCE;
	}

	@Override
	protected Planet createElement(ITranslator translator, String planetId, String language, String moduleName)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(planetId, NAME, language);

			final Set<Faction> factions;
			try {
				factions = getCommaSeparatedValues(planetId, FACTION, language, moduleName,
						FactionsFactory.getInstance());
			} catch (InvalidXmlElementException ixe) {
				throw new InvalidPlanetException("Error in planet '" + planetId
						+ "' structure. Invalid faction defintion.", ixe);
			}

			return new Planet(planetId, name, language, moduleName, factions);
		} catch (Exception e) {
			throw new InvalidPlanetException("Invalid structure in planet '" + planetId + "'.", e);
		}
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}
}