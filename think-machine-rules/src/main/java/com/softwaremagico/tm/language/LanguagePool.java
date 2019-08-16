package com.softwaremagico.tm.language;

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
import java.util.HashMap;
import java.util.Map;

import com.softwaremagico.tm.file.Path;

public class LanguagePool {

	private static Map<String, Map<String, ITranslator>> existingTranslators = new HashMap<>();

	private LanguagePool() {
	}

	public static ITranslator getTranslator(String xmlFile, String moduleName) {
		if (existingTranslators.get(moduleName) == null) {
			existingTranslators.put(moduleName, new HashMap<String, ITranslator>());
		}
		ITranslator translator = existingTranslators.get(moduleName).get(xmlFile);
		if (translator == null) {
			final File file = Translator.getTranslatorPath(xmlFile, moduleName);
			if (file != null && file.exists()) {
				// Get from folder
				translator = new Translator(file.getPath());
				existingTranslators.get(moduleName).put(xmlFile, translator);
			} else {
				// Get from resources
				translator = new Translator(LanguagePool.class.getClassLoader()
						.getResource(Path.MODULES_FOLDER + File.separator + xmlFile).toString());
				existingTranslators.get(moduleName).put(xmlFile, translator);
			}
		}
		return translator;
	}

	public static void clearCache() {
		existingTranslators = new HashMap<>();
	}
}
