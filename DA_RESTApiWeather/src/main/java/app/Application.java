package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import db.WeatherDB;
import db.WeatherSingletonDB;
import domain.WeatherData;

@Configuration
@EnableScheduling
@SpringBootApplication
public class Application {
	
	private static WeatherDB weatherDB = WeatherSingletonDB.getDB();
	private static final String APPID = "5c9880ceeeb837f3e97bc298698fd501";
	private static BufferedReader br;

	@Bean
	public Executor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	public static void main(String[] args) throws Exception {
		calculateWeather("/");
	}

	// "0 0 * * * *" = the top of every hour of every day.
	// @Scheduled(cron = "0 0 * * * *")
	// 10minutes = 600000 milisec
	@Scheduled(fixedRate = 600000)
	public static WeatherDB calculateWeather(String currentURL) throws RestClientException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		String url;
		WeatherData weather;
		//System.out.println("Current relative path is: " + currentURL);
		//URL urlZip = new URL(currentURL + "/DA_RESTApiWeather/vlaanderen-zip.txt");
		URL urlZip = new URL("http://java.cyclone2.khleuven.be:38034/r0298778_DA_DisplaySiteToTestOwnRESTAPI/vlaanderen-zip.txt");
		br = new BufferedReader(new InputStreamReader(urlZip.openStream()));
		//br = new BufferedReader(new FileReader("vlaanderen-zip.txt"));
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
		System.out.println(weatherDB.getStatistics());
		return weatherDB;
	}
	
	public static WeatherDB getWeather() throws RestClientException, IOException {
		return weatherDB;
	}

}
