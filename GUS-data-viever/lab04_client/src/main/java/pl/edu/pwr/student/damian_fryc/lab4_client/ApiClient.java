package pl.edu.pwr.student.damian_fryc.lab4_client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

// ApiClient will get data once per category and will sort data locally,
// filtering data will be on server side (not great)
// TOO MUCH DATA, on top of the table will be "<-- 0 -->", number will be a text input field

// saving by CacheHandler, but it saves and reads only JSONArray's so the data must be marged

// GUI input order
// category -> area -> variable -> section -> period -> year

public class ApiClient {
	private static final String url = "https://api-dbw.stat.gov.pl/api/";
	public enum Language {
		PL,
		EN
	}

	private String language;
	public ApiClient(){
		this.language = getLanguage(Language.PL);
	}
	public JSONObject getData(int variableId, int sectionId, int yearId, int periodId) {
		String request = "variable/variable-data-section?id-zmienna="+variableId
				+"&id-przekroj="+sectionId
				+"&id-rok="+yearId
				+"&id-okres="+periodId
				+"&ile-na-stronie=5000&numer-strony=0&lang="+this.language;
		JSONArray data = fetchData(request);

		JSONArray wayOfPresentation = getWayOfPresentation();
		JSONArray flags = getFlags();
		JSONArray variableSectionPositions = getVariableSectionPositions(sectionId);
		if(data.isEmpty())
			return  null;

		return JsonParser.getFinalData(data, wayOfPresentation, flags, variableSectionPositions, language);
	}
	public JSONObject getData(int variableId, int sectionId, int yearId, int periodId, String filter) {
		JSONObject data = getData(variableId, sectionId, yearId, periodId);
		if(data == null || Objects.equals(filter, ""))
			return data;
		return JsonParser.filterData(data, filter);
	}
	public JSONArray getCategories() {
		String request = "area/area-area?lang="+this.language;
		return JsonParser.getCategories(fetchData(request));
	}
	public JSONArray getAreas(int categoryId) {
		String request = "area/area-area?lang="+this.language;
		return JsonParser.getAreas(fetchData(request), categoryId);
	}
	public JSONArray getVariables(int areaId) {
		String request = "area/area-variable?id-obszaru="+areaId+"&lang="+this.language;
		return JsonParser.getVariables(fetchData(request));
	}
	public JSONArray getSections(int variableId) {
		String request = "variable/variable-section-periods?ile-na-stronie=5000&numer-strony=0&lang="+this.language;
		return JsonParser.getSections(fetchData(request), variableId);
	}
	public JSONArray getPeriods(int sectionId, int variableId) {
		String request1 = "variable/variable-section-periods?ile-na-stronie=5000&numer-strony=0&lang="+this.language;
		String request2 = "dictionaries/periods-dictionary?ile-na-stronie=5000&numer-strony=0&lang="+this.language;
		return JsonParser.getPeriods(fetchData(request1), fetchData(request2), sectionId, variableId);
	}
	public void setLanguage(String language){
		this.language = language;
	}
	private String getLanguage(Language language){
		if(language == Language.EN) return "en";
		else return "pl";
	}
	private synchronized JSONArray fetchData(String request) {
		String fileRequest = request;
		JSONArray data = CacheHandler.readCache(fileRequest);
		if (data != null)
			return data;

		JSONArray finalData = new JSONArray();
		JSONArray bannedRequests = CacheHandler.getBannedRequestList();
		for (int i = 0; i < bannedRequests.length(); i++) {
			if (bannedRequests.get(i).equals(request)) {
				return finalData;
			}
		}

		// Getting data from server as JSONArray, asks for all pages of data
		try(HttpClient client = HttpClient.newHttpClient()) {
			int i = 0;
			JSONObject dataObject = new JSONObject();
			JSONArray dataArray;
			do {
				request = request.replaceFirst("numer-strony=\\d+", "numer-strony=" + i);
				HttpRequest httpRequest = HttpRequest.newBuilder()
						.uri(new URI(url + request))
						.build();
				HttpResponse<String> responseData = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

				if (responseData.statusCode() != 200) {
					CacheHandler.banRequest(request);
					return finalData;
				}

				// Make JSONObject or make JSONArray and put all the data to final array
				try {
					dataObject = new JSONObject(responseData.body());
				}catch (JSONException ignored){}

				if (dataObject.isEmpty()) dataArray = new JSONArray(responseData.body());
				else dataArray = dataObject.getJSONArray("data");
				for (int j = 0; j < dataArray.length(); j++)
					finalData.put(dataArray.get(j));
				i++;
			} while (!dataObject.isEmpty() && (dataObject.get("page-number") != dataObject.get("page-count")) && dataArray.length() == 5000);
			CacheHandler.saveToCache(fileRequest, finalData);
			wait(500);
		}
		catch(URISyntaxException | InterruptedException | IOException e){
			System.out.println(e.getMessage());
		}
		return finalData;
	}
	private JSONArray getWayOfPresentation() {
		String request = "dictionaries/way-of-presentation?page=0&page-size=5000&lang="+this.language;
		return fetchData(request);
	}
	private JSONArray getFlags() {
		String request = "dictionaries/flag-dictionary?page=0&page-size=5000&lang="+this.language;
		return fetchData(request);
	}
	private JSONArray getVariableSectionPositions(int sectionId) {
		String request = "variable/variable-section-position?id-przekroj="+sectionId+"&lang="+this.language;
		return fetchData(request);
	}

	public static void main(String[] args) {
		ApiClient test = new ApiClient();

		JSONObject finalData = test.getData(506, 869, 2023, 247, "adsdadjhb21=12d=21d12\n\n\n23-1232d=21d\n2d2==daasda==2dd\n1>=500");

		System.out.printf("%-10s %-40s %-20s %-40s %-10s %-10s %-10s%n",
				"Nr", "Pozycja 1", "Pozycja 2", "Pozycja 3", "Wartość", "Miara", "Flaga");
		System.out.println("=".repeat(140));

		// Wiersze danych
		for (int i = 0; i < finalData.getJSONArray("data").length(); i++) {
			JSONArray item = finalData.getJSONArray("data").getJSONArray(i);
			System.out.printf("%-10s %-40s %-20s %-40s %-10s %-10s %-10s%n",
					item.get(0),
					item.get(1),
					item.get(2),
					item.get(3),
					item.get(4),
					item.get(5),
					item.get(6));
		}
	}
}
