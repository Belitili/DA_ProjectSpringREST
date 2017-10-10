package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import app.Application;
import db.WeatherDB;
import domain.WeatherData;

/**
 * Servlet implementation class WeatherREST
 */
@RestController
@WebServlet("/WeatherController")
public class WeatherREST extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			this.handleRequest(request, response);
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			this.handleRequest(request, response);
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, RestClientException, URISyntaxException {
		Application.calculateWeather(this.getCurrentUrl(request));
		String action = request.getParameter("action");
		if (action != null) {
			switch (action) {
			case "weather":
				getWeather(request, response);
				break;
			case "stats":
				getStats(request, response);
				break;
			}
		}
	}

	private void getWeather(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WeatherDB db = Application.getWeather();
		Collection<WeatherData> coll = db.getAll().values();
		List<WeatherData> list;
		if (coll instanceof List) {
			list = (List<WeatherData>) coll;
		} else {
			list = new ArrayList<WeatherData>(coll);
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(list);

		JSONArray array = new JSONArray();
		for (WeatherData wd : list) {
			try {
				ObjectMapper jackmapper = new ObjectMapper();
				String json = jackmapper.writeValueAsString(wd);
				JSONObject jsonObj = new JSONObject(json);
				array.put(jsonObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// setting the response type to json
		response.setContentType("application/json");
		// setting the CORS request, CrossOriginResourceSharing
		// so that this url/response is accessible in another domain (angular
		// application for example)
		response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println(array.toString());
		response.getWriter().write(array.toString());
	}

	private void getStats(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WeatherDB db = Application.getWeather();
		Map<String, Double> stats = db.getStatistics();

		String jsonString = new Gson().toJson(stats);
		// setting the response type to json
		response.setContentType("application/json");
		// setting the CORS request, CrossOriginResourceSharing
		// so that this url/response is accessible in another domain (angular
		// application for example)
		response.setHeader("Access-Control-Allow-Origin", "*");

		response.getWriter().write(jsonString);
	}

	private static String getCurrentUrl(HttpServletRequest request) throws URISyntaxException, MalformedURLException {

		URL url = new URL(request.getRequestURL().toString());

		String host = url.getHost();
		String userInfo = url.getUserInfo();
		String scheme = url.getProtocol();
		int port = url.getPort();
		String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
		String query = (String) request.getAttribute("javax.servlet.forward.query_string");
		URI uri = new URI(scheme, userInfo, host, port, path, query, null);
		return uri.toString();
	}

}
