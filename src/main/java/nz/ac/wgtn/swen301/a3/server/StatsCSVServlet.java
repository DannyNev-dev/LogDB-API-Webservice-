package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatsCSVServlet extends HttpServlet{

	private static final long serialVersionUID = -277787920703387054L;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/csv");
		PrintWriter writer = resp.getWriter();
		List<String[]> dataLines = new ArrayList<>();
		dataLines.add(new String[] { "logger", "ALL", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "TRACE", "OFF" });
		
		Map<String, int[]> map = Persistency.getLoggerCountsMap();
		for(String s:map.keySet()) {
			String[] temp = new String[9];
			temp[0] = s;
			for(int i = 1; i < map.get(s).length+1;i++) {
				temp[i] = Integer.toString(map.get(s)[i-1]);
			}
			dataLines.add(temp);
		}
		
		dataLines.stream().map(this::convertToCSV).forEach(writer::println);
		writer.close();
		resp.setStatus(200);
	}
	
	/**
	 * Method created with help from an online tutorial from https://www.baeldung.com/java-csv
	 * Simply maps the given array to a string using the escapeSpecialCharacters method to format string correctly
	 * @param data
	 * @return
	 */
	private String convertToCSV(String[] data) {
	    return Stream.of(data).map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining("\t"));
	}
	
	/**
	 * Replaces characters that aren't valid and add's slashes and quotes where needed (Time saver deluxe)
	 * @param data
	 * @return
	 */
	private String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	
}
