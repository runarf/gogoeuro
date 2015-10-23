package bri.tney.spears.goeuro;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.opencsv.CSVWriter;

public class DeveloperTest {
// randomm
	public static void stringForm(URL url) {
		HttpURLConnection conn = null;
		BufferedReader rd = null;
		StringBuilder sb = new StringBuilder();
		String line;
		JSONObject place = null;
		CSVWriter writer = null;
		JSONArray toJson = null;
		String csv = "data.csv";
		String record;
		String[] csvRecord;

		try {
			conn = (HttpURLConnection) url.openConnection();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} finally {
				rd.close();
			}
		} catch (IOException e) {
			System.out.println("Caught IOexception: " + e.getMessage());
		} finally {
			conn.disconnect();
		}

		// System.out.println(sb.toString());
		// System.out.println(toJson.toString());

		toJson = new JSONArray(sb.toString());
		try {
			writer = new CSVWriter(new FileWriter(csv));
			try {
				for (int i = 0; i < toJson.length(); i++) {

					place = toJson.getJSONObject(i);
					record = Integer.toString(place.getInt("_id")) + "#" + place.get("name") + "#" + place.get("type")
							+ "#" + Double.toString(place.getJSONObject("geo_position").getDouble("latitude")) + "#"
							+ Double.toString(place.getJSONObject("geo_position").getDouble("latitude"));
					System.out.println(record);
					csvRecord = record.split("#");
					writer.writeNext(csvRecord);
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("IO exception");
		}
	}

	public static void csvForm(URL url) {

		JSONTokener tokener = null;
		JSONArray jsonArray = null;
		JSONObject place = null;
		String record;
		String[] csvRecord;
		String csvFileName = "data.csv";

		try (CSVWriter writer = new CSVWriter(new FileWriter(csvFileName))) {
			tokener = new JSONTokener(url.openStream());
			jsonArray = new JSONArray(tokener);

			// Get relevant data and write it in CSV format to file
			for (int i = 0; i < jsonArray.length(); i++) {
				place = jsonArray.getJSONObject(i);
				record = Integer.toString(place.getInt("_id")) + "#" + place.get("name") + "#" + place.get("type") + "#"
						+ Double.toString(place.getJSONObject("geo_position").getDouble("latitude")) + "#"
						+ Double.toString(place.getJSONObject("geo_position").getDouble("latitude"));
				System.out.println(record);
				csvRecord = record.split("#");
				writer.writeNext(csvRecord);
			}

		} catch (IOException e) {
			System.err.println("Caught IOexception: " + e.getMessage());
		}

	}

	public static void main(String[] args) {
		String place = null;
		URL url = null;
		if (args.length == 0) {
			System.out.println("Proper Usage is: place name");
			System.exit(0);
		}
		try {
			place = URLEncoder.encode(args[0], "UTF-8");
			url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/" + place);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Caught UnsupportedEncodingException: " + e.getMessage());
		} catch (MalformedURLException e) {
			System.out.println("The URL " + url + " is malformed");
		}
		System.out.println(url);
		// stringForm(url);
		csvForm(url);

	}

}
