package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherGUI {
    private JPanel panel1;
    private JTextField textFieldCity;
    private JButton checkWeatherButton;
    private JTextField textFieldCityName;
    private JTextField textFieldTime;
    private JTextField textFieldWeather;
    private JTextField textFieldWindSpeed;
    private JTextField textFieldWindDirection;
    private JTextField textFieldTemperature;
    private JTextField textFieldPressure;
    private JTextField textFieldTemperatureFeel;
    private JTextField textFieldHumidity;
    private static final String IMAGE_PATH_ONE = "src/main/resources/kompas.png";
    private static final String IMAGE_PATH_TWO = "src/main/resources/pogoda.png";
    private JLabel imageLabel;
    private JLabel imageLabel2;

    public WeatherGUI() {
        setImageLabel();

        checkWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = textFieldCity.getText();

                if (city.isEmpty()) {
                    JOptionPane.showMessageDialog(panel1, "Please enter a city name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!city.matches("[a-zA-Z\\s]+")) {
                    JOptionPane.showMessageDialog(panel1, "Invalid city name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String apiKey = "d4ffff00ad86e11962a08a89e0825be4";
                String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(response -> {
                            WeatherAPI weatherData = parse(response);
                            if (weatherData != null) {
                                setDataToGUI(weatherData);
                            }
                        })
                        .join();
            }
        });
    }

    private WeatherAPI parse(String responseBody) {
        try {
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

            return new WeatherAPI(cityName, temperature, temperatureFeels, pressure, humidity, speed, deg,
                    weatherMain, weatherDescription, unixTime);
        } catch (Exception e) {
            return null;
        }
    }

    private void setDataToGUI(WeatherAPI data) {
        textFieldTemperature.setText(String.format("%.2f", data.temperature) + "°C");
        textFieldTemperatureFeel.setText(String.format("%.2f", data.temperatureFeels) + "°C");
        textFieldPressure.setText(data.pressure + "hPa");
        textFieldHumidity.setText(data.humidity + "%");
        textFieldWeather.setText(data.weatherMain + " (" + data.weatherDescription + ")");
        textFieldWindSpeed.setText(String.format("%.2f", data.speed) + "m/s");
        textFieldWindDirection.setText(data.windDirection);
        textFieldCityName.setText(data.cityName);
        textFieldTime.setText(data.time);
    }

    private void setImageLabel() {
        try {
            BufferedImage image = ImageIO.read(new File(IMAGE_PATH_ONE));
            BufferedImage imageTwo = ImageIO.read(new File(IMAGE_PATH_TWO));
            Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            Image scaledImageTwo = imageTwo.getScaledInstance(300, 150, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            ImageIcon imageIconTwo = new ImageIcon(scaledImageTwo);
            imageLabel.setIcon(imageIcon);
            imageLabel2.setIcon(imageIconTwo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("WeatherGUI");
        frame.setContentPane(new WeatherGUI().panel1);
        frame.setPreferredSize(new Dimension(500, 430));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBackground(new Color(-1));
        panel1.setForeground(new Color(-1));
        textFieldCity = new JTextField();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldCity, gbc);
        textFieldCityName = new JTextField();
        textFieldCityName.setEditable(false);
        textFieldCityName.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldCityName, gbc);
        textFieldWeather = new JTextField();
        textFieldWeather.setEditable(false);
        textFieldWeather.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldWeather, gbc);
        textFieldWindSpeed = new JTextField();
        textFieldWindSpeed.setEditable(false);
        textFieldWindSpeed.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldWindSpeed, gbc);
        textFieldWindDirection = new JTextField();
        textFieldWindDirection.setEditable(false);
        textFieldWindDirection.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldWindDirection, gbc);
        imageLabel = new JLabel();
        imageLabel.setEnabled(true);
        imageLabel.setHorizontalAlignment(0);
        imageLabel.setHorizontalTextPosition(0);
        imageLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridheight = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(imageLabel, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Chose city to check");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalTextPosition(0);
        label2.setText("Wind Direction");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        panel1.add(label2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        textFieldTime = new JTextField();
        textFieldTime.setEditable(false);
        textFieldTime.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.weightx = 1.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldTime, gbc);
        imageLabel2 = new JLabel();
        imageLabel2.setBackground(new Color(-2104859));
        imageLabel2.setEnabled(true);
        imageLabel2.setHorizontalAlignment(0);
        imageLabel2.setHorizontalTextPosition(0);
        imageLabel2.setIconTextGap(4);
        imageLabel2.setRequestFocusEnabled(false);
        imageLabel2.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(imageLabel2, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalTextPosition(0);
        label3.setText("Wind Speed");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel1.add(label3, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer5, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Temperature");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 15;
        panel1.add(label4, gbc);
        textFieldTemperature = new JTextField();
        textFieldTemperature.setEditable(false);
        textFieldTemperature.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 16;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldTemperature, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Temperature feeling");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 17;
        panel1.add(label5, gbc);
        textFieldTemperatureFeel = new JTextField();
        textFieldTemperatureFeel.setEditable(false);
        textFieldTemperatureFeel.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 18;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldTemperatureFeel, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Pressure");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 15;
        panel1.add(label6, gbc);
        textFieldPressure = new JTextField();
        textFieldPressure.setEditable(false);
        textFieldPressure.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldPressure, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Humidity");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 17;
        panel1.add(label7, gbc);
        textFieldHumidity = new JTextField();
        textFieldHumidity.setEditable(false);
        textFieldHumidity.setHorizontalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(textFieldHumidity, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 19;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer9, gbc);
        checkWeatherButton = new JButton();
        checkWeatherButton.setText("Check Weather");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(checkWeatherButton, gbc);
        label1.setLabelFor(textFieldCity);
        label2.setLabelFor(textFieldWindDirection);
        label3.setLabelFor(textFieldWindSpeed);
        label4.setLabelFor(textFieldTemperature);
        label5.setLabelFor(textFieldTemperatureFeel);
        label6.setLabelFor(textFieldPressure);
        label7.setLabelFor(textFieldHumidity);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
