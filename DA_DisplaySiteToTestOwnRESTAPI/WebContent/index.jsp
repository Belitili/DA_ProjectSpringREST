<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display REST API</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body onload="showWeather(); showStatistics();">

	<h1>Statistics</h1>
	<div id="statistics"></div>

	<h1>Overzicht Weer Vlaamse Gemeentes</h1>
	<div id="weather"></div>
	
	<%@include file="footer.jspf" %>

	<script type="text/javascript">
		function showWeather() {
			var weatherDiv = document.getElementById("weather");
			console.log('reached here 1');
			weatherDiv.innerHTML = " ";
			$.getJSON("http://localhost:8080/DA_RESTApiWeather/WeatherController?action=weather",
					function(result) {
						console.log('reached here 2');
						console.log('result: ' + result);
						var weatherDescrip = " ";
						$.each(result, function(i, weatherData) {
							var weatherInfo = " ";
							weatherInfo += "<div class=\"weatherBox\">";
							weatherInfo = "<h3> " + weatherData.name + ", " + weatherData.sys.country +  "</h3>";
							weatherInfo += "Coordinates: " + weatherData.coord.lon + ", " + weatherData.coord.lon + "<br>";
							weatherInfo += "Temperature: " + weatherData.main.temp + "°C <br>";
							weatherInfo += "Pressure: " + weatherData.main.pressure + "<br>";
							weatherInfo += "Humidity: " + weatherData.main.humidity + "<br>";
							weatherInfo += "Wind Speed: " + weatherData.wind.speed + "<br>";
							weatherInfo += "</div>"
							weatherDescrip += weatherInfo;
							console.log(weatherDescrip)
						});
						$('#weather').html(weatherDescrip);
						//updates every 10min
						setTimeout(showWeather, 600000);
					});
		}
		function showStatistics() {
			var statisticsDiv = document.getElementById("statistics");
			console.log('statistics reached here 1');
			statisticsDiv.innerHTML = " ";
			$.getJSON("http://localhost:8080/DA_RESTApiWeather/WeatherController?action=stats",
					function(result) {
						console.log('statistics reached here 2');
						var statisticsDescrip = " ";
						$.each(result, function(key, value) {
							var statInfo = " ";
							statInfo += key + ": " + value + "<br>";
							statisticsDescrip += statInfo;
						});
						console.log(statisticsDescrip);
						$('#statistics').html(statisticsDescrip);
						//updates every 10min
						setTimeout(showWeather, 600000);
					});
		}
	</script>

</body>
</html>