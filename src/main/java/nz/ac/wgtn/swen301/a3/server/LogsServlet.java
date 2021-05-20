package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author danma
 *
 */
public class LogsServlet extends HttpServlet{

	private static final long serialVersionUID = 6209606058369376152L;
	
	//index of level names, is relevant when handling get request (0..7) 0 being the least Specific and 7 being no logs
	private static final String[] LevelNames = new String[] {"ALL","DEBUG","INFO","WARN","ERROR","FATAL","TRACE","OFF"};
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {}
	
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {}
	
}

