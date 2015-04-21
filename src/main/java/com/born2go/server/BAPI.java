package com.born2go.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.born2go.shared.Path;
import com.born2go.shared.Trip;
import com.google.gson.Gson;

public class BAPI extends HttpServlet implements Servlet{

	private static final long serialVersionUID = 1L;
	DataServiceImpl dataService = new DataServiceImpl();
	
	/**
	 * Supported Actions:
	 * getTrip: Return json data
	 * getPath: Return html
	 * getSetting: Return json data of current version, min mobile app version, adv rate (%) ....
	 * 			{"version":"10","minversion":"7","advrate":"0"}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String result = "";
		String action = req.getParameter("action");
		
		try {
			String id = req.getParameter("id");
			
			if (action.compareToIgnoreCase("getPath") == 0){
				result = getPathContentByID(Long.parseLong(id));
				resp.setContentType("text/html; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getTrip") == 0){
				result = getTripByID(Long.parseLong(id));
				resp.setContentType("application/json; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getAllPost") == 0){
				result = getAllPostByTripID(Long.parseLong(id));
				resp.setContentType("application/json; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getSetting") == 0){
				resp.setContentType("application/json; charset=utf-8");
				result = getSetting();
			}
			else{
				result = "This command is not supported yet. Check again!";
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.getWriter().write(result);
	}
	
    //NOTICE NOTICE NOTICE
	//GAE supports gzip natively, so code here for reference only
	// Choose GZIP if the header includes "gzip"
	/*PrintWriter out = null;

	String encodings = req.getHeader("Accept-Encoding");
    //Test code
    if (encodings == null) encodings = "gzip, deflate, sdch";
    
    if (encodings != null && encodings.indexOf("gzip") != -1) {
      // Go with GZIP
      resp.setHeader("Content-Encoding", "gzip");
      out = new PrintWriter(new GZIPOutputStream(resp.getOutputStream()),false);
      result = "Trying ZIP! " + result;
    }
    else {
      // No compression
      out = resp.getWriter();
      result = "No Encoding! " + encodings + result;
    }
    resp.setHeader("Vary", "Accept-Encoding");
    resp.setHeader("Born2Go", "Welcome");
    out.write(result);*/
	
	/**
	 * 
	 * @param id
	 * 	Long value, id of Path
	 * @return
	 * 	Page html show the Path or Error
	 */
	private String getPathContentByID(Long id){
		String html = "";
		Path path = dataService.findPart(id);
		if (path != null){
			html = "<h2>" + path.getTitle() + "</h2><br>";
			
			html = path.getDescription();
		}
		else{
			html = "<h2>" + id + "</h2><br> This post is not exist. That's all we know :(";
		}
		return html;
	}
	/**
	 * 
	 * @param id
	 * 	Long value, id of Trip
	 * @return
	 * 	Page json show the Trip or Error (if status is false)
	 */
	private String getTripByID(Long id){
		String json = "";
		Trip trip = dataService.findTrip(id);
		if (trip != null){
			Gson gson = new Gson();
			json = gson.toJson(trip);
		} else{
			json = "{\"status\": false,\"error\": \"Trip ID: " + id + "doesn't exist\"}";
		}
		return json;
	}
	
	private String getAllPostByTripID(Long id){
		String json = "";
		String html = "";
		Trip trip = dataService.findTrip(id);
		if (trip != null){
			List<Long> ids = trip.getDestination();
			List<Path> paths = dataService.listOfPath(ids);
			List<String> posts = new ArrayList<String>();
			
			Gson gson = new Gson();
			
			for(Path path: paths){
				if (path != null){
					html = "<h2>" + path.getTitle() + "</h2><br>";
					
					html = path.getDescription();
				}
				else{
					html = "<h2>" + id + "</h2><br> This post is not exist. That's all we know :(";
				}
				posts.add(html);
			}
			json = gson.toJson(posts);
		} else{
			json = "{\"status\": false,\"error\": \"Trip ID: " + id + "doesn't exist\"}";
		}
		return json;
	}

	private String getSetting(){
		String result = "";
		result = "{\"version\":\"10\",\"minversion\":\"7\",\"advrate\":\"0\"}";//Test setting only
		return result;
	}
}
