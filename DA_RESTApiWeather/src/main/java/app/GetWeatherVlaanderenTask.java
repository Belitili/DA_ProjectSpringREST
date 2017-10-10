package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import db.WeatherDB;
import db.WeatherSingletonDB;
import domain.WeatherData;

@Component
@EnableScheduling
public class GetWeatherVlaanderenTask {

	// Ex:
	// http://api.openweathermap.org/data/2.5/weather?zip=2000,be&APPID=5c9880ceeeb837f3e97bc298698fd501
	private static final String APPID = "5c9880ceeeb837f3e97bc298698fd501";

	private BufferedReader br;

	private WeatherDB weatherDB = WeatherSingletonDB.getDB();

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public void run(RestTemplate restTemplate) throws RestClientException, IOException {
		String url;
		WeatherData weather;
		br = new BufferedReader(new FileReader("vlaanderen-zip.txt"));
		String line = null;
		// Does first 3 zips to test
		int i = 0;
		while ((line = br.readLine()) != null && i < 3) {
			url = "http://api.openweathermap.org/data/2.5/weather?zip=" + line + ",be&APPID=" + APPID + "&units=metric";
			weather = restTemplate.getForObject(url, WeatherData.class);
			weatherDB.add(line, weather);
			System.out.println(weather);
			i++;
		}
	}
}