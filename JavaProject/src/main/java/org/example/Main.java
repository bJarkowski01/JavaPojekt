package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String cityName = scanner.nextLine();

        String apiKey = "d4ffff00ad86e11962a08a89e0825be4";
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", cityName, apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                //.uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q=Londyn&appid=d4ffff00ad86e11962a08a89e0825be4"))
                .uri(URI.create(url))
                .build();
        //HttpResponse<String> response;
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                //.thenAccept(System.out::println)
                .thenAccept(response -> {WeatherAPI weatherData = parse(response);})
                .join();
    }

    public static WeatherAPI parse(String responseBody)
    {
        JSONObject jsonObject = new JSONObject(responseBody);
        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temperature = main.getDouble("temp") - 273.15; // Convert Kelvin to Celsius
        double temperatureFeels = main.getDouble("feels_like") - 273.15;
        int pressure = main.getInt("pressure");
        int humidity = main.getInt("humidity");
        JSONObject wind = jsonObject.getJSONObject("wind");
        double speed = wind.getDouble("speed");
        double deg = wind.getInt("deg");

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        String weatherMain = weather.getString("main");
        String weatherDescription = weather.getString("description");
        long unixTime = jsonObject.getLong("dt");

        StringBuilder parsedData = new StringBuilder();
        parsedData.append("City: ").append(cityName).append("\n")
                .append("Temperature: ").append(String.format("%.2f", temperature)).append("°C\n")
                .append("Temperature feels like: ").append(String.format("%.2f", temperatureFeels)).append("°C\n")
                .append("Pressure: ").append(pressure).append(" hPa\n")
                .append("Humidity: ").append(humidity).append("%\n")
                .append("Weather: ").append(weatherMain).append(" (").append(weatherDescription).append(")\n")
                .append("Wind speed: ").append(String.format("%.2f", speed)).append("m/s\n")
                .append("Wind direction: ").append(String.format("%.2f", deg));

        System.out.println(parsedData.toString());

        //return parsedData.toString();
        return new WeatherAPI(cityName, temperature, temperatureFeels, pressure, humidity, speed, deg, weatherMain, weatherDescription, unixTime);
    }

}