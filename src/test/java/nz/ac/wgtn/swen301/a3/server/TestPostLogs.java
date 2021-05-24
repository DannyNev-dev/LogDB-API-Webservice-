package nz.ac.wgtn.swen301.a3.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestPostLogs {
	
	static {
		MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
		LogsServlet service = new LogsServlet();
        service.doDelete(request, response);
	}
	
	@Test
    public void testInvalidRequestResponseCode1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // query parameter missing
        LogsServlet service = new LogsServlet();
        service.doPost(request,response);

        assertEquals(400,response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("not a valid param name","42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doPost(request,response);

        assertEquals(400,response.getStatus());
    }

    @Test
    public void testValidRequestResponseCode() throws IOException {
        String jsonString = generateTestLogJson();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(jsonString.getBytes("utf-8"));

        LogsServlet service = new LogsServlet();
        service.doDelete(request, response);
        service.doPost(request,response);

        assertEquals(201,response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {
        String jsonString = generateTestLogJson();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(jsonString.getBytes("utf-8"));
        
        LogsServlet service = new LogsServlet();
        service.doDelete(request, response);
        service.doPost(request,response);
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setParameter("limit","10");
        request.setParameter("level","DEBUG");
        
        service.doGet(request, response);
        
        assertTrue(response.getContentType().startsWith("application/json"));
    }
    
    /**
     * Tests that we can get the log event we posted to the server and that it matches
     * @throws Exception 
     */
    @Test
    public void testReturnedValues() throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
        String jsonString = generateTestLogJson();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        LogsServlet service = new LogsServlet();
        
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(jsonString.getBytes("utf-8"));
        
        service.doDelete(request, response);
        service.doPost(request,response);
        
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        request.setParameter("limit","10");
        request.setParameter("level","DEBUG");
        
        service.doGet(request,response);
        
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
    
    /**
     * Returns a simple test log json object represented as a string
     * @return
     */
    private String generateTestLogJson() {
    	return "{\"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\" ,"
        		+ "\"message\": \"application started\" ,"
        		+ "\"timestamp\":\"04-05-2021 10:12:00\","
        		+ "\"thread\":\"main\","
        		+ "\"logger\":\"com.example.Foo\","
        		+ "\"level\":\"DEBUG\","
        		+ "\"errorDetails\":\"string\"}";
    }
}
