package com.softwaremagico.tm.pdf.skills.occultism;

/*-
 * #%L
 * The Thinking Machine (Core)
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
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class OccultismTable extends LateralHeaderPdfPTable {
	private final static int ROW_WIDTH = 70;
	private final static float[] widths = { 1f, 6f };

	public OccultismTable() {
		super(widths);
		addCell(createLateralVerticalTitle("Ocultismo", 1));
		addCell(createContent());
		setWidthPercentage(100);
		getDefaultCell().setPadding(0);
		setSpacingAfter(0);
		setSpacingBefore(0);
	}

	@Override
	protected PdfPCell createLateralVerticalTitle(String title, int rowspan) {
		PdfPCell titleCell = super.createLateralVerticalTitle(title, rowspan);
		titleCell.setMinimumHeight(ROW_WIDTH);
		return titleCell;
	}

	private PdfPCell createContent() {
		float[] widths = { 3f, 1f, 1f, 3f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		PdfPCell psiTitleCell = new PdfPCell(new Phrase("Psi", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		psiTitleCell.setBorder(0);
		// psiTitleCell.setMinimumHeight(30);
		psiTitleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(psiTitleCell);

		table.addCell(createRectangle());
		table.addCell(createRectangle());

		PdfPCell urgeTitleCell = new PdfPCell(new Phrase("Ansia", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		urgeTitleCell.setBorder(0);
		urgeTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(urgeTitleCell);

		PdfPCell teurgyTitleCell = new PdfPCell(new Phrase("Teúrgia", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		teurgyTitleCell.setBorder(0);
		// eurgyTitleCell.setMinimumHeight(30);
		teurgyTitleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(teurgyTitleCell);

		table.addCell(createRectangle());
		table.addCell(createRectangle());

		PdfPCell hubrisTitleCell = new PdfPCell(new Phrase("Soberbia", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		hubrisTitleCell.setBorder(0);
		hubrisTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(hubrisTitleCell);

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		cell.setPadding(0);
		BaseElement.setCellProperties(cell);

		return cell;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.OCCULSTISM_TITLE_FONT_SIZE;
	}

}
