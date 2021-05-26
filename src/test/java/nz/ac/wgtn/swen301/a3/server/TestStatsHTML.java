package nz.ac.wgtn.swen301.a3.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestStatsHTML {
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
        StatsServlet service = new StatsServlet();
        service.doGet(request,response);
        assertEquals(200,response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        StatsServlet service = new StatsServlet();
        service.doGet(request,response);
        assertTrue(response.getContentType().startsWith("text/html"));
    }

    @Test
    public void testReturnedValues() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        StatsServlet service = new StatsServlet();
        service.doGet(request,response);
        String respString = response.getContentAsString();
        assertTrue(respString.contains("logger"));
        assertTrue(respString.contains("com.example.Foo"));
        assertTrue(respString.contains("1"));
        assertTrue(respString.contains("0"));
        
    }
}
