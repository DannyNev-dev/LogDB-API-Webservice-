package nz.ac.wgtn.swen301.a3.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class TestStatsXLS {
	static {
		//clear DB before testing
		LogsServlet ls = new LogsServlet();
		ls.doDelete(new MockHttpServletRequest(), new MockHttpServletResponse());
		
		LogEvent l = new LogEvent();
		l.setId("d290f1ee-6c54-4b01-90e6-d701748f0851");
		l.setMessage("application started");
		l.setTimestamp("04-05-2021 10:12:00");
		l.setLogger("com.example.Foo");
		l.setLevel("DEBUG");
		l.setThread("main");
		l.setErrorDetails("string");
		Persistency.DB.add(l);
	}
    @Test
    public void testValidRequestResponseCode() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        StatsXLSServlet service = new StatsXLSServlet();
        service.doGet(request,response);
        assertEquals(200,response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        StatsXLSServlet service = new StatsXLSServlet();
        service.doGet(request,response);
        assertTrue(response.getContentType().startsWith("application/vnd.ms-excel"));
    }

    @Test
    public void testReturnedValues() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        StatsXLSServlet service = new StatsXLSServlet();
        service.doGet(request,response);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getContentAsByteArray());
        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet s = wb.getSheet("stats");
        Row r = s.getRow(1);
        assertTrue(r.getCell(0).getStringCellValue().equals("com.example.Foo"));
        assertTrue(r.getCell(1).getNumericCellValue()==0);
        assertTrue(r.getCell(2).getNumericCellValue()==1);
    }
}
