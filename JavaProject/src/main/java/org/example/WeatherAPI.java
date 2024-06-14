package org.example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherAPI {
    public String cityName;
    public double temperature;
    public double temperatureFeels;
    public int pressure;
    public int humidity;
    public double speed;
    public String windDirection;
    public String weatherMain;
    public String weatherDescription;
    public String time;

    public WeatherAPI(String _cityName, double _temp, double _tempFeels, int _pressure, int _humidity, double _speed, double _deg, String _weather, String _description, long _unixTime)
    {
        cityName = _cityName;
        temperature = _temp;
        temperatureFeels = _tempFeels;
        pressure = _pressure;
        humidity = _humidity;
        speed = _speed;
        windDirection = getWindDirection(_deg);
        weatherMain = _weather;
        weatherDescription = _description;
        time = convertUnixTime(_unixTime);
    }

    public static String getWindDirection(double deg)
    {
        if (deg > 337.5 || deg <= 22.25) {
            return "N";
        } else if (deg > 22.25 && deg <= 67.5) {
            return "NE";
        } else if (deg > 67.5 && deg <= 112.5) {
            return "E";
        } else if (deg > 112.5 && deg <= 157.5) {
            return "SE";
        } else if (deg > 157.5 && deg <= 202.5) {
            return "S";
        } else if (deg > 202.5 && deg <= 247.5) {
            return "SW";
        } else if (deg > 247.5 && deg <= 292.5) {
            return "W";
        } else if (deg > 292.5 && deg <= 337.5) {
            return "NW";
        }
        return "No data";
    }

    private String convertUnixTime(long unixTime) {
        Instant instant = Instant.ofEpochSecond(unixTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
