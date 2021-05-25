package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatsXLSServlet extends HttpServlet {

	private static final long serialVersionUID = -277787920703387054L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/vnd.ms-excel");
		Workbook wb = createXMLWorkbook();
		wb.write(resp.getOutputStream());
		wb.close();
		resp.setStatus(200);
	}

	/**
	 * Creates and fills an excel single sheet work book with the Persistency.DB
	 * statistics
	 * 
	 * @return
	 */
	private Workbook createXMLWorkbook() {

		Workbook workBook = new XSSFWorkbook();
		Sheet sheet = workBook.createSheet("stats");
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workBook.createCellStyle();

		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workBook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("logger");
		headerCell.setCellStyle(headerStyle);

		for (int i = 1; i < LogsServlet.LevelNames.size() + 1; i++) {
			headerCell = header.createCell(i);
			headerCell.setCellValue(LogsServlet.LevelNames.get(i - 1));
			headerCell.setCellStyle(headerStyle);
		}
		// Add the log event statistics to the table
		CellStyle style = workBook.createCellStyle();
		style.setWrapText(true);

		for (int i = 1; i < Persistency.getLoggers().size() + 1; i++) {

			String logger = Persistency.getLoggers().get(i - 1);
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0);
			cell.setCellValue(logger);

			List<Integer> list = Persistency.getLoggerCountsMap().get(logger);
			for (int j = 1; j < list.size() + 1; j++) {
				cell = row.createCell(j);
				cell.setCellValue(list.get(j - 1));
				cell.setCellStyle(style);
			}
		}

		return workBook;
	}

}
