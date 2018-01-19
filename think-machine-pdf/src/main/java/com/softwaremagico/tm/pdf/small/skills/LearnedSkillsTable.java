package com.softwaremagico.tm.pdf.small.skills;

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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.skills.SkillsTable;

public class LearnedSkillsTable extends SkillsTable {
	private final static int ROWS = 20;
	private final static int MAX_SKILL_COLUMN_WIDTH = 115;

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.addCell(getSkillsColumnTable(characterPlayer, language));
		return table;
	}

	private static PdfPCell getSkillsColumnTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		table.addCell(createCompactTitle(getTranslator().getTranslatedText("learnedSkills"), FadingSunsTheme.CHARACTER_SMALL_SKILLS_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (AvailableSkill skill : characterPlayer.getLearnedSkills()) {
				if (characterPlayer.getSkillRanks(skill) > 0) {
					table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH));
					table.addCell(createSkillValue(characterPlayer.getSkillRanks(skill), characterPlayer.isSkillSpecial(skill),
							FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
					added++;
				}
			}

			for (int i = added; i < ROWS; i++) {
				for (int j = 0; j < widths.length; j++) {
					table.addCell(new Paragraph(" "));
				}
			}
		}

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

}
