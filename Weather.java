import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly";

    public static void main(String[] args) {
        String city = "London,us"; 

        try {
            String data = getWeatherData(city);
            if (data != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    System.out.println("\n1. Get weather\n2. Get Wind Speed\n3. Get Pressure\n0. Exit");
                    System.out.print("Enter your choice: ");
                    String choice = reader.readLine();

                    if (choice.equals("1")) {
                        System.out.print("Enter the date (yyyy-mm-dd): ");
                        String date = reader.readLine();
                        String temperature = getWeatherByDate(data, date);
                        if (temperature != null) {
                            System.out.println("The temperature on " + date + " is " + temperature + "°F.");
                        } else {
                            System.out.println("No data found for the given date.");
                        }
                    } else if (choice.equals("2")) {
                        System.out.print("Enter the date (yyyy-mm-dd): ");
                        String date = reader.readLine();
                        String windSpeed = getWindSpeedByDate(data, date);
                        if (windSpeed != null) {
                            System.out.println("The wind speed on " + date + " is " + windSpeed + " m/s.");
                        } else {
                            System.out.println("No data found for the given date.");
                        }
                    } else if (choice.equals("3")) {
                        System.out.print("Enter the date (yyyy-mm-dd): ");
                        String date = reader.readLine();
                        String pressure = getPressureByDate(data, date);
                        if (pressure != null) {
                            System.out.println("The pressure on " + date + " is " + pressure + " hPa.");
                        } else {
                            System.out.println("No data found for the given date.");
                        }
                    } else if (choice.equals("0")) {
                        System.out.println("Exiting the program.");
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static String getWeatherData(String city) throws IOException {
        URL url = new URL(BASE_URL + "?q=" + city + "&appid=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("Failed to fetch weather data.");
            return null;
        }
    }

    private static String getWeatherByDate(String data, String date) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String dateTime = item.getString("dt_txt");

            if (dateTime.startsWith(date)) {
                JSONObject main = item.getJSONObject("main");
                double temperature = main.getDouble("temp");
                return String.format("%.2f", temperature);
            }
        }
        return null;
    }

    private static String getWindSpeedByDate(String data, String date) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String dateTime = item.getString("dt_txt");

            if (dateTime.startsWith(date)) {
                JSONObject wind = item.getJSONObject("wind");
                double windSpeed = wind.getDouble("speed");
                return String.format("%.2f", windSpeed);
            }
        }
        return null;
    }

    private static String getPressureByDate(String data, String date) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String dateTime = item.getString("dt_txt");

            if (dateTime.startsWith(date)) {
                JSONObject main = item.getJSONObject("main");
                double pressure = main.getDouble("pressure");
                return String.format("%.2f", pressure);
            }
        }
        return null;
    }
}
