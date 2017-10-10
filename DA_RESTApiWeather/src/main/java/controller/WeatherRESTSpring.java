package controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import db.WeatherDB;
import db.WeatherSingletonDB;
import domain.WeatherData;

@RestController
public class WeatherRESTSpring {
	
	WeatherDB db =  WeatherSingletonDB.getDB();
	
	@RequestMapping("/") //Welcome page, non-rest
    public String welcome() {
         String string = "Welcome to Spring RestTemplate Example.";
         string += "\n/weatherSpring for all weather data in flanders";
         string += "\n/statsSpring for all weather statistics in flanders";
         string += "\n/statsSpring/{zip} for weather data per zip in flanders";
         return string;
    }

	@RequestMapping(value = "/weatherSpring", method = RequestMethod.GET) //REST Endpoint
	public Collection<WeatherData> getAllWeather() {
		return db.getAll().values();
	}
	
	@RequestMapping(value = "/statsSpring", method = RequestMethod.GET) //REST Endpoint
	public Map<String, Double> getStats() {
		return db.getStatistics();
	}
	
	@RequestMapping(value = "/weatherSpring/{zip}", method = RequestMethod.GET) //REST Endpoint
    public WeatherData getWeather(@PathVariable String zip) {
		return db.get(zip);
    }

}
