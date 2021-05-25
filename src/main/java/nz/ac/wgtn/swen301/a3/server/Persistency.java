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
	public static Map<String,List<Integer>> getLoggerCountsMap(){
		List<String> loggers = Persistency.getLoggers();
		Map<String,List<Integer>> map = new HashMap<>();
		if(loggers.size()>=1) {
			for(String logger:loggers) {
				map.put(logger, getLevelCounts(Persistency.DB.stream().filter(e->!e.getLogger().equals(logger)).collect(Collectors.toList())));
			}
		}
		return map;
	}
	
	/**
	 * Returns counts of the different levels in the standard level order (ALL = 0, OFF=7)
	 * @param list of logs to count
	 * @return
	 */
	public static List<Integer> getLevelCounts(List<LogEvent> logs){
		List<Integer> counts = new ArrayList<>(LogsServlet.LevelNames.size());
		logs.stream().forEach(e -> {
			if(e.getLevel().equals("ALL")) {
				counts.set(0, counts.get(0)+1);
			}else if(e.getLevel().equals("DEBUG")) {
				counts.set(1, counts.get(1)+1);
			}else if(e.getLevel().equals("INFO")) {
				counts.set(2, counts.get(2)+1);
			}else if(e.getLevel().equals("WARN")) {
				counts.set(3, counts.get(3)+1);
			}else if(e.getLevel().equals("ERROR")) {
				counts.set(4, counts.get(4)+1);
			}else if(e.getLevel().equals("FATAL")) {
				counts.set(5, counts.get(5)+1);
			}else if(e.getLevel().equals("TRACE")) {
				counts.set(6, counts.get(6)+1);
			}else if(e.getLevel().equals("OFF")) {
				counts.set(7,counts.get(7)+1);
			}
		});
		return counts;
	}
}
