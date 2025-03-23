package pl.edu.pwr.student.damian_fryc.lab4_client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    // add exceptions if bad JSONArrays
    private static String findStringById(JSONArray jsonArray, String idName, int id, String keyName){
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject item = jsonArray.getJSONObject(j);
            if (item.getInt(idName) == id) {
                return item.getString(keyName);
            }
        }
        return null;
    }
	static JSONArray getCategories(JSONArray areas){
        JSONArray categories = new JSONArray();
        Set<Integer> uniqueCategories = new HashSet<>();

        for (int i = 0; i < areas.length(); i++) {
            JSONObject item = areas.getJSONObject(i);
            if (item.getBoolean("czy-zmienne") && !uniqueCategories.contains(item.getInt("id-poziom"))) {
                JSONObject selectedItem = new JSONObject();
                uniqueCategories.add(item.getInt("id-poziom"));
                selectedItem.put("id", item.getInt("id-poziom"));
                selectedItem.put("nazwa", item.getString("nazwa-poziom"));
                categories.put(selectedItem);
            }
        }
        return categories;
    }
    static JSONArray getAreas(JSONArray areas, int categoryId){
        JSONArray areass = new JSONArray();

        for (int i = 0; i < areas.length(); i++) {
            JSONObject item = areas.getJSONObject(i);
            if (item.getBoolean("czy-zmienne") && item.getInt("id-poziom") == categoryId) {
                JSONObject selectedItem = new JSONObject();
                selectedItem.put("id", item.getInt("id"));
                selectedItem.put("nazwa", item.getString("nazwa"));
                areass.put(selectedItem);
            }
        }
        return areass;
    }
    static JSONArray getVariables(JSONArray areaVariables){
        JSONArray variables = new JSONArray();
        for (int i = 0; i < areaVariables.length(); i++) {
            JSONObject item = areaVariables.getJSONObject(i);
            JSONObject selectedItem = new JSONObject();
            selectedItem.put("id", item.getInt("id-zmienna"));
            selectedItem.put("nazwa", item.getString("nazwa-zmienna"));
            variables.put(selectedItem);
        }
        return variables;
    }
    static JSONArray getSections(JSONArray variableSectionPeriods, int variableId){
        JSONArray sections = new JSONArray();
        Set<Integer> uniqueSections = new HashSet<>();
        for (int i = 0; i < variableSectionPeriods.length(); i++) {
            JSONObject item = variableSectionPeriods.getJSONObject(i);
            if (item.getInt("id-zmienna") == variableId && !uniqueSections.contains(item.getInt("id-przekroj"))) {
                JSONObject selectedItem = new JSONObject();
                uniqueSections.add(item.getInt("id-przekroj"));
                selectedItem.put("id", item.getInt("id-przekroj"));
                selectedItem.put("nazwa", item.getString("nazwa-przekroj"));
                sections.put(selectedItem);
            }
        }
        return sections;
    }
    static JSONArray getPeriods(JSONArray variableSectionPeriods, JSONArray periodsDictionary, int sectionId, int variableId){
        JSONArray periods = new JSONArray();
        for (int i = 0; i < variableSectionPeriods.length(); i++) {
            JSONObject item = variableSectionPeriods.getJSONObject(i);
            if (item.getInt("id-przekroj") == sectionId && item.getInt("id-zmienna") == variableId) {
                JSONObject selectedItem = new JSONObject();
                selectedItem.put("id", item.getInt("id-okres"));

                    // add period data
                String periodData = findStringById(periodsDictionary, "id-okres", item.getInt("id-okres"), "opis");
                selectedItem.put("nazwa", periodData);

                periods.put(selectedItem);
            }
        }
        return periods;
    }

    static JSONObject getFinalData(JSONArray finalData, JSONArray wayOfPresentation, JSONArray flags, JSONArray variableSectionPositions, String language){
        JSONObject finalObject = new JSONObject();

        // Saving headers
        JSONArray headersForData = new JSONArray();
        headersForData.put("id");
        headersForData.put((Objects.equals(language, "pl"))?"Wartosci":"Values");
        headersForData.put(wayOfPresentation.getJSONObject(0).getString("nazwa-jednostki"));
        int k = 1;
        do {
            String dimensionName = findStringById(variableSectionPositions, "id-pozycja", finalData.getJSONObject(0).getInt("id-pozycja-"+k), "nazwa-wymiar");
            headersForData.put(dimensionName);
            k++;
        }while(finalData.getJSONObject(0).has("id-pozycja-"+k));
        headersForData.put((Objects.equals(language, "pl"))?"Flagi":"Flags");
        finalObject.put("headers", headersForData);

        // Saving data into array
        JSONArray decodedFinalData = new JSONArray();
        for (int i = 0; i < finalData.length(); i++) {
            JSONObject item = finalData.getJSONObject(i);
            JSONArray selectedItem = new JSONArray();
            selectedItem.put(item.getInt("rownumber"));

            selectedItem.put(item.getDouble("wartosc"));
            String presentationData = findStringById(wayOfPresentation, "id-sposob-prezentacji-miara", item.getInt("id-sposob-prezentacji-miara"), "nazwa");
            selectedItem.put(presentationData);

            int j = 1;
            do {
                String positionData = findStringById(variableSectionPositions, "id-pozycja", item.getInt("id-pozycja-"+j), "nazwa-pozycja");
                selectedItem.put(positionData);
                j++;
            }while(item.has("id-pozycja-"+j));

            String flagData = findStringById(flags, "id-flaga", item.getInt("id-flaga"), "oznaczenie");
            selectedItem.put(flagData);

            decodedFinalData.put(selectedItem);
        }
        finalObject.put("data",decodedFinalData);
        return finalObject;
    }

    public static JSONObject filterData(JSONObject data, String filters) {
        JSONArray headersForData = data.getJSONArray("headers");
        JSONArray notFilteredData = data.getJSONArray("data");

        JSONObject finalObject = new JSONObject();
        JSONArray filteredData = new JSONArray();

        String[] filtersSplit = filters.split("\n");
        ArrayList<ArrayList<Object>> filtersA = new ArrayList<>();
        // column, operation, comparisonValue

        String regex = "^(\\d+)(!_=\\*|!@=\\*|!=\\*|@=\\*|_=\\*|==\\*|!_=|!@=|!=|@=|_=|>=|>|<=|<|==)(.*)";
        Pattern pattern = Pattern.compile(regex);

        // create filters, filter not correct - skip
        for (String s : filtersSplit) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                try {
                    int number = Integer.parseInt(matcher.group(1));
                    if (number < 0 || number > notFilteredData.getJSONArray(0).length())
                        continue;
                    String operator = matcher.group(2);
                    String comparisonValue = matcher.group(3);

                    ArrayList<Object> newFilter = new ArrayList<>();
                    newFilter.add(number);
                    newFilter.add(operator);
                    newFilter.add(comparisonValue);

                    filtersA.add(newFilter);

                } catch (IndexOutOfBoundsException ignored) {}
            }
        }

        // pass only correct data
        for (int i = 0; i < notFilteredData.length(); i++) {
            JSONArray row = notFilteredData.getJSONArray(i);
            boolean passing = true;

            for (ArrayList<Object> filter : filtersA) {
                boolean didPassFilter = DataFilter.applyFilter(row.get((Integer) filter.getFirst()), (String) filter.get(1), (String) filter.get(2));
                if (!didPassFilter) {
                    passing = false;
                    break;
                }
            }
            if(passing)
                filteredData.put(row);
        }



        finalObject.put("headers",headersForData);
        finalObject.put("data",filteredData);
        return finalObject;
    }
}
