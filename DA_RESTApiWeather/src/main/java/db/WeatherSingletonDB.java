package db;

import java.util.HashMap;
import java.util.Map;

import domain.WeatherData;

//Singleton DB
public final class WeatherSingletonDB extends WeatherDB {
	
	private static volatile WeatherSingletonDB instance = null;
	private Map<String, WeatherData> weatherData = new HashMap<String, WeatherData>();

	private WeatherSingletonDB() {
		super();
	}
	
	public static WeatherDB getDB() {
		if (instance == null) {
			synchronized(WeatherSingletonDB.class) {
				if (instance == null) {
					instance = new WeatherSingletonDB();
				}
			}
		}
		return instance;
	}

	@Override
	public void add(String zip, WeatherData data) {
		weatherData.put(zip, data);
		super.calculateStats();
	}
	
	@Override
	public WeatherData get(String zip) {
		return weatherData.get(zip);
	}
	
	@Override
	public Map<String, WeatherData> getAll() {
		return weatherData;
	}
	
}
