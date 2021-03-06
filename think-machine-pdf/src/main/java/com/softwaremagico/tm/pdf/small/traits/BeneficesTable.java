package com.softwaremagico.tm.pdf.small.traits;

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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class BeneficesTable extends VerticalTable {
	private static final String GAP = "__________________";
	private static final int TRAIT_COLUMN_WIDTH = 60;
	private static final float[] WIDTHS = { 1f };
	private static final int ROWS = 10;

	public BeneficesTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("beneficesTable"), FadingSunsTheme.CHARACTER_SMALL_BENEFICES_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (final AvailableBenefice benefit : characterPlayer.getAllBenefices()) {
				if (benefit.getSpecialization() != null) {
					addCell(createElementLine(benefit.getSpecialization().getName(), TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				} else {
					addCell(createElementLine(benefit.getName(), TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				}
				added++;
			}
		}

		if (characterPlayer == null) {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(GAP, TRAIT_COLUMN_WIDTH));
			}
		} else {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			}
		}
	}

}
