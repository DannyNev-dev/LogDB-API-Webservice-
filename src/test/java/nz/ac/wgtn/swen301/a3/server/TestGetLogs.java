package nz.ac.wgtn.swen301.a3.server;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetLogs {
	static {
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
    public void testInvalidRequestResponseCode1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // query parameter missing
        LogsServlet service = new LogsServlet();
        service.doGet(request,response);

        assertEquals(400,response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("not a valid param name","42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doGet(request,response);

        assertEquals(400,response.getStatus());
    }

    @Test
    public void testValidRequestResponseCode() throws IOException {
    	
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit","30");
        request.setParameter("level","OFF");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request,response);

        assertEquals(200,response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit","30");
        request.setParameter("level","OFF");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request,response);

        assertTrue(response.getContentType().startsWith("application/json"));
    }

    @Test
    public void testReturnedValues() throws IOException {
    	
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit","30");
        request.setParameter("level","DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request,response);
        ObjectMapper mapper = new ObjectMapper();
        String result = response.getContentAsString();
        JsonNode node = mapper.readTree(result);
        JsonNode innerNode = node.get(0);
        JsonNode id = innerNode.get("id");
        JsonNode message = innerNode.get("message");
        JsonNode timeStamp = innerNode.get("timestamp");
        JsonNode thread = innerNode.get("thread");
        JsonNode logger = innerNode.get("logger");
        JsonNode level = innerNode.get("level");
        JsonNode errorDetails = innerNode.get("errorDetails");

        assertTrue(id.asText().equals("d290f1ee-6c54-4b01-90e6-d701748f0851"));
        assertTrue(message.asText().equals("application started"));
        assertTrue(timeStamp.asText().equals("04-05-2021 10:12:00"));
        assertTrue(logger.asText().equals("com.example.Foo"));
        assertTrue(level.asText().equals("DEBUG"));
        assertTrue(thread.asText().equals("main"));
        assertTrue(errorDetails.asText().equals("string"));
        
    }
}
