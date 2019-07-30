package com.softwaremagico.tm.pdf.complete.characteristics;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.rules.language.Translator;

public class CharacteristicsTableFactory extends BaseElement {

	public static PdfPTable getCharacteristicsBasicsTable(CharacterPlayer characterPlayer) {
		final float[] widths = { 1f, 1f, 1f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		final PdfPCell separator = createSeparator();
		separator.setColspan(widths.length);
		table.addCell(separator);

		final Phrase content = new Phrase(getTranslator().getTranslatedText("characteristics").toUpperCase(), new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.TITLE_FONT_SIZE));
		final PdfPCell titleCell = new PdfPCell(content);
		setCellProperties(titleCell);
		titleCell.setColspan(widths.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setFixedHeight(30);
		table.addCell(titleCell);
		table.getDefaultCell().setPadding(0);

		for (final CharacteristicType type : CharacteristicType.values()) {
			try {
				table.addCell(new CharacteristicsColumn(characterPlayer, type, CharacteristicsDefinitionFactory.getInstance().getAll(type,
						Translator.getLanguage())));
			} catch (NullPointerException npe) {
				PdfExporterLog.errorMessage(CharacteristicsTableFactory.class.getName(), npe);
			}
		}

		return table;
	}
}
