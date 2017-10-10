package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import domain.WeatherData;

public abstract class WeatherDB {
	
	public Map<String, Double> statistics;
	
	abstract public void add(String zip, WeatherData data);
	
	abstract public WeatherData get(String zip);
	
	abstract public Map<String, WeatherData> getAll();
	
	public Map<String, Double> getStatistics() {
		return statistics;
	}
	
	public double getAverageWindSpeed() {
		double totalWindSpeed = 0.0;
        for (WeatherData weather: this.getAll().values()) {
            double speed = weather.getWind().getSpeed();
            totalWindSpeed += speed;
        }
        double avgWind = totalWindSpeed/getAll().size();
        return this.roundTo2Digits(avgWind);
	}
	
	public void calculateStats() {
		this.statistics = new HashMap<String, Double>();
		Collection<WeatherData> data = this.getAll().values();
		double totalTemp = 0.0;
		double totalWindSpeed = 0.0;
		double totalHumidity = 0.0;
        for (WeatherData weather: data) {
            double temp = weather.getMain().getTemp();
            totalTemp += temp;
            double speed = weather.getWind().getSpeed();
            totalWindSpeed += speed;
            double humidity = weather.getMain().getHumidity();
            totalHumidity += humidity;
        }
        double avgTemp = totalTemp/data.size();
        this.statistics.put("Average Temperature", this.roundTo2Digits(avgTemp));
        double avgWind = totalWindSpeed/data.size();
        this.statistics.put("Average Wind", this.roundTo2Digits(avgWind));
        double avgHumidity = totalHumidity/data.size();
        this.statistics.put("Average Humidity", this.roundTo2Digits(avgHumidity));
	}
	
	public Map<String, Double> getStats() {
		return statistics;
	}
	
	private double roundTo2Digits(double arg) {
		return Math.round((arg * 100.0) / 100.0);
	}
	
	/* private double getAvg_to2digits(ArrayList<Double> args) {
		double total = 0.0;
        for (double arg: args) {
            total += arg;
        }
        double avg = total / args.size();
        return this.roundTo2Digits(avg);
	} */
	
}
