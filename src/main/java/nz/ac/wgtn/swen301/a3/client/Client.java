/**
 * 
 */
package nz.ac.wgtn.swen301.a3.client;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author nevilldani
 *
 */
public class Client {
	
	private static final String BASE_URL = "http://localhost:8080/resthome4logs/";

	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length!=2) {
			throw new Error("No Parameters provided");
		}	
		String format = args[0];
		String fileString = args[1];
		Response response;
        FileOutputStream outputStream = new FileOutputStream(fileString);
		switch(format) {
		case "excel":
			response = getResponse("/statsxls");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(response.body().bytes());
	        Workbook wb = WorkbookFactory.create(inputStream);
	        wb.write(outputStream);
			return;
		case "csv":
			response = getResponse("/statscsv");
			String csvString = response.body().string();
			outputStream.write(csvString.getBytes());
			return;
		}
		outputStream.close();
	}
	
	private static Response getResponse(String servletMap) throws IOException {
		OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder()
	    	      .url(BASE_URL + servletMap)
	    	      .build();

	    Call call = client.newCall(request);
	    return call.execute();
	}

}
