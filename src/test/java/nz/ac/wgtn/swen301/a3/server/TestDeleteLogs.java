package nz.ac.wgtn.swen301.a3.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestDeleteLogs {
	
	@Test
	public void doDelete() {
		LogsServlet ls = new LogsServlet();
		ls.doDelete(new MockHttpServletRequest(), new MockHttpServletResponse());
		assertTrue(Persistency.DB.isEmpty());
	}
    @Test
    public void testValidRequestResponseCode() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doDelete(request,response);

        assertEquals(200,response.getStatus());
    }
}
