package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Persistency {
	public static List<LogEvent> DB = new ArrayList<LogEvent>(); 
	
	/**
	 * Gets a list of the loggers within the DB
	 * @return
	 */
	public static List<String> getLoggers(){
		List<String> loggers = new ArrayList<String>();
		if(!Persistency.DB.isEmpty()) {
			Persistency.DB.stream().forEach(e -> {if(!loggers.contains(e.getLogger())){loggers.add(e.getLogger());}});
		}
		return loggers;
	}
	
	/**
	 * returns a map of loggers and their corresponding level counts
	 * @return
	 */
	public static Map<String, int[]> getLoggerCountsMap(){
		List<String> loggers = Persistency.getLoggers();
		Map<String,int[]> map = new HashMap<>();
		if(loggers.size()>=1) {
			for(String logger:loggers) {
				map.put(logger, getLevelCounts(Persistency.DB.stream().filter(e->e.getLogger().equals(logger)).collect(Collectors.toList())));
			}
		}
		return map;
	}
	
	/**
	 * Returns counts of the different levels in the standard level order (ALL = 0, OFF=7)
	 * @param list of logs to count
	 * @return
	 */
	public static int[] getLevelCounts(List<LogEvent> logs){
		int[] counts = new int[LogsServlet.LevelNames.size()];
		for(LogEvent e:logs) {
			if(e.getLevel().equals("ALL")) {
				counts[0]++;
			}else if(e.getLevel().equals("DEBUG")) {
				counts[1]++;
			}else if(e.getLevel().equals("INFO")) {
				counts[2]++;
			}else if(e.getLevel().equals("WARN")) {
				counts[3]++;
			}else if(e.getLevel().equals("ERROR")) {
				counts[4]++;
			}else if(e.getLevel().equals("FATAL")) {
				counts[5]++;
			}else if(e.getLevel().equals("TRACE")) {
				counts[6]++;
			}else if(e.getLevel().equals("OFF")) {
				counts[7]++;
			}
		}
		return counts;
	}
}
