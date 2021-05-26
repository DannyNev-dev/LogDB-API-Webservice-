package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatsServlet extends HttpServlet{

	private static final long serialVersionUID = -277787920703387054L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<style>");
		out.println("table, th, td { border: 1px solid black; border-collapse: collapse; }");
		out.println("th, td { padding: 15px; text-align: left; }");
		out.println("</style>");
		out.println("</head>");
        out.println("<body>");
        out.println("<table style=\"width:100%\">");
        out.println("<tr>");
        out.println("<th>logger</th>");
        out.println("<th>ALL</th> ");
        out.println("<th>DEBUG</th>");
        out.println("<th>INFO</th>");
        out.println("<th>WARN</th>");
        out.println("<th>ERROR</th>");
        out.println("<th>FATAL</th>");
        out.println("<th>TRACE</th>");
        out.println("<th>OFF</th>");
        out.println("</tr>");
        out.println(createHTMLtdString());
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();
        resp.setStatus(200);
	}
	
	/**
	 * Creates the td (value rows) for the html table and returns it as on large string containing all the necassery \n and html tags
	 * @return
	 */
	private String createHTMLtdString() {
		String result = "";
		Map<String,List<Integer>> map = Persistency.getLoggerCountsMap();
		for(String ln : map.keySet()) {
			String temp = "<tr>\n";
			temp += "<td>"+ln+"</td>\n";
			for(int i = 0;i<map.get(ln).size();i++) {
				temp += "<td>" + Integer.toString(map.get(ln).get(i))+"</td>\n";
			}
			result += temp + "</tr>\n";
		}
		return result;
	}
	
}
