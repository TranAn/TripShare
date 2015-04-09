package com.born2go.server;

import java.io.IOException;

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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String result = "";
		String action = req.getParameter("action");
		
		try {
			String id = req.getParameter("id");
			
			if (action.compareToIgnoreCase("getPath") == 0){
				result = getPathContentByID(Long.parseLong(id));
				resp.setContentType("text/html");
				resp.getWriter().write(result);
			}
			else if (action.compareToIgnoreCase("getTrip") == 0){
				result = getTripByID(Long.parseLong(id));
				resp.setContentType("text/json");
				resp.getWriter().write(result);
			}
			else{
				resp.getWriter().write("This command is not supported yet. Check again!");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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
			html = "<h2>" + id + "</h2><br> This path is not exist. That's all we know :(";
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
}
