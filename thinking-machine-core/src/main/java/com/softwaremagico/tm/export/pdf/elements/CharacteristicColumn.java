package com.softwaremagico.tm.export.pdf.elements;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.export.pdf.FadingSunsTheme;

public class CharacteristicColumn extends PdfPTable {

	private final static float[] widths = { 1f, 5f};

	public CharacteristicColumn(String title, String[] content) {
		super(widths);
		addCell(createTitle(title, content.length));
		addCell(createContent(content));
	}

	private PdfPCell createTitle(String text, int rowspan) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase(text, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRotation(90);
		titleCell.setRowspan(rowspan);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return titleCell;
	}

	private PdfPCell createContent(String[] content) {
		float[] widths = { 3f, 1f};
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		for (String text : content) {
			table.addCell(new Phrase(text, new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			
			PdfPCell box = new PdfPCell();
			box.setMinimumHeight(10);
			box.setBorderWidthTop(0.5f);
			box.setBorderWidthLeft(0.5f);
			box.setBorderWidthRight(0.5f);
			box.setBorderWidthBottom(0.5f);
			table.addCell(box);
		}

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		BaseElement.setCellProperties(cell);

		return cell;
	}

}
