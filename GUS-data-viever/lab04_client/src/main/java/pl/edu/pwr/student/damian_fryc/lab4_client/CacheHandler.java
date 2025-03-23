package pl.edu.pwr.student.damian_fryc.lab4_client;
import org.json.JSONArray;

import java.io.*;
import java.util.Scanner;

public class CacheHandler {
	public static JSONArray readCache(String request) {
		File file = new File(parseRequestToFileName(request));
		if (!file.exists()) return null;

		String content = readFileContent(file);
        return new JSONArray(content);
	}

	public static void saveToCache(String request, JSONArray data) {
		File file = new File(parseRequestToFileName(request));
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(data.toString(4));
		} catch (IOException ignored) {}
	}
	private static String parseRequestToFileName(String request){
		String fileName;
		fileName = request.replaceAll("/", "_");
		fileName = fileName.replaceFirst("[&?](ile-na-stronie|page-size)=\\d+","");
		fileName = fileName.replaceFirst("[&?](numer-strony|page)=\\d+","");
		fileName = fileName.replaceFirst("[&?]lang=","_");
		fileName = fileName.replaceAll("\\?", "%");
		return "Cache/"+fileName+".json";
	}

	private static String readFileContent(File file) {
		StringBuilder content = new StringBuilder();
		try (Scanner reader = new Scanner(new FileReader(file))) {
			while (reader.hasNextLine()) {
				content.append(reader.nextLine()).append("\n");
			}
		} catch (FileNotFoundException ignored) {}
		return content.toString().trim();
	}

    public static void banRequest(String request) {
		JSONArray bannedRequests = getBannedRequestList();
        bannedRequests.put(request);
		File file = new File(parseRequestToFileName("banned_requests"));
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(bannedRequests.toString(4));
		} catch (IOException e) {
			System.err.println("Couldn't write to banned_requests file: " + e.getMessage());
		}
    }
	public static JSONArray getBannedRequestList() {
		File file = new File(parseRequestToFileName("banned_requests"));
		if (!file.exists()) return new JSONArray();
		String content = readFileContent(file);

        return new JSONArray(content);
	}


}
