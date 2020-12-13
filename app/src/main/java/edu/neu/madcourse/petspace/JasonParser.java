package edu.neu.madcourse.petspace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JasonParser {
    private HashMap<String, String> parseJasonObject(JSONObject object) {
        //Initialize hasp map
        HashMap<String, String> dataList = new HashMap<>();
        try{
            //Get name from object
            String name = object.getString("name");
            //Get latitude from object
            String latitude = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            //Get longitude from object
            String longitude = object.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //Put all value in hash map
            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //Return hash map
        return dataList;
    }
    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        //Initialize hash map list
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                //Initialize hash map
                HashMap<String, String> data = parseJasonObject((JSONObject) jsonArray.get(i));
                //Add data in the hash map list
                dataList.add(data);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Return hash map list
        return dataList;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        //Initialize jason array
        JSONArray jsonArray = null;
        //Get result array
        try {
            jsonArray = object.getJSONArray(("results"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return array
        return parseJsonArray(jsonArray);
    }
}