package bri.tney.spears.goeuro;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.opencsv.CSVWriter;

public class DeveloperTest {

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
			place = URLEncoder.encode(args[0], "UTF-8").replace("+", "%20");
			System.out.println(place);
			url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/" + place);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Caught UnsupportedEncodingException: " + e.getMessage());
		} catch (MalformedURLException e) {
			System.out.println("The URL " + url + " is malformed");
		}
		System.out.println(url);
		csvForm(url);

	}

}
