package com.softwaremagico.tm.character.equipment.weapons;

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
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;

public class AccessoryFactory extends XmlFactory<Accessory> {
    private static final String TRANSLATOR_FILE = "weapons_accessories.xml";

    private static class AccessoryFactoryInit {
        public static final AccessoryFactory INSTANCE = new AccessoryFactory();
    }

    public static AccessoryFactory getInstance() {
        return AccessoryFactoryInit.INSTANCE;
    }

    @Override
    protected String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    protected Accessory createElement(ITranslator translator, String accessoryId, String name, String description,
                                      String language, String moduleName)
            throws InvalidXmlElementException {
        return new Accessory(accessoryId, name, description, language, moduleName);
    }
}
