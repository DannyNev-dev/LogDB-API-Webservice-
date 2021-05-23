package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LogsServlet extends HttpServlet{

	private static final long serialVersionUID = 6209606058369376152L;
	private static final List<String> LevelNames = new ArrayList<String>(Arrays.asList("ALL","DEBUG","INFO","WARN","ERROR","FATAL","TRACE","OFF"));
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String limit = req.getParameter("limit");
		String level = req.getParameter("level");
		
		if(level==null || limit==null) {
			resp.sendError(400, "No Parameters given");
		}
		else {
			int limit_num;
			try{ limit_num=Integer.parseInt(limit); }
			catch(NumberFormatException e) { limit_num=0; }
			
			if(limit_num > 0 && LogsServlet.LevelNames.contains(level)) {
				resp.setContentType("application/json");
				PrintWriter out = resp.getWriter();
				
				out.println("<html>");
		        out.println("<body>");  
		        
		        ArrayList<LogEvent> logs = new ArrayList<>();
		        ObjectMapper mapper = new ObjectMapper();
		    	ArrayNode arrayNode = mapper.createArrayNode();
		    	
		        if(!level.equals("OFF")) {
		        	logs = getLogWithLevel(limit_num,level);
		        	if(!logs.isEmpty()) {
		        		logs = sortLogsByDate(logs);
		        		List<JsonNode> l = new ArrayList<>();
						for(LogEvent logEvent:logs) {			
							l.add(mapper.readTree(format(logEvent)));
						}
				    	arrayNode.addAll(l);	
		        	}
		        }			    			    	
		        
			    out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode));
		        out.println("</body>");
		        out.println("</html>");
		        out.close();
			}
			else {
				resp.sendError(400, "Parameters given were invalid");
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {}
	
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {}
	
	/**
	 * returns log events from Persistency.DB with the given level or less
	 * @param limit
	 * @param level
	 * @return
	 */
	private ArrayList<LogEvent> getLogWithLevel(int limit, String level){
		ArrayList<LogEvent> tempLogs = new ArrayList<LogEvent>();;
    	for(int i = 0;i<limit;i++) {
    		int count = 0;
    		for(LogEvent l: Persistency.DB) {		        			
    			if((LevelNames.indexOf(l.level) >= LevelNames.indexOf(level)) && !tempLogs.contains(l)) {
    				tempLogs.add(l);
    				break;
    			}
    			count++; //count wont increment when found, if the last log in list matches, count != DB.size()
    		}
    		if(count==Persistency.DB.size()) {break;} //if no log matches from db return empty list
    	}
    	return tempLogs;
	}
	/**
	 * sorts the given list of log events by their timestamp
	 * @param logs
	 * @return sorted list
	 */
	private ArrayList<LogEvent> sortLogsByDate(ArrayList<LogEvent> logs){	
	    Comparator<LogEvent> byTimeStamp = Comparator.comparing(LogEvent::getTimeAsDate,(ts1, ts2) -> ts1.compareTo(ts2));
	    Collections.sort(logs, byTimeStamp);
	    return logs;
	}
	/**
	 * Converts a log event into a formatted json string
	 * @param event
	 * @return
	 */
	private String format(LogEvent event)
    {
		String id = event.getId();
		String message = event.getMessage();
		String timeStamp = event.getTimestamp();
		String thread = event.getThread();
		String logger = event.getLogger();
		String level = event.getLevel();
		String errorDetails = event.getErrorDetails();			
		String json = "FAILED";
		try {
		    ObjectMapper mapper = new ObjectMapper();
		    ObjectNode user = mapper.createObjectNode();
		    user.put("id", id);
		    user.put("message", message);
		    user.put("timestamp", timeStamp);
		    user.put("thread", thread);
		    user.put("logger", logger);
		    user.put("level", level);	  
		    user.put("errorDetails", errorDetails);
		    json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);	    
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
        return json;
    }

}