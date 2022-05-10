package com.example.civiladvocacy;

import android.content.DialogInterface;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class OfficialNames implements Runnable {
    private MainActivity mainActivity;

    private String location;
    private static final String DATA_URL =
            "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String yourAPIKey =
            "AIzaSyBFknki15J7QkuKUkDID2dHKK-4doajoCQ";

    public OfficialNames(MainActivity mainActivity, String location) {
        this.mainActivity = mainActivity;
        this.location = location;
    }

    @Override
    public void run() {
        String finalURL;
        //System.out.println("We are in here and the string is " + location);
        String[] splited = location.split("\\s+");
        String[] splited2 = location.split(",");
        String regex = "[0-9]+";
        String connector = "&address=";
        //System.out.println("The size is " + splited.length);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(location);


        //if all we get is a zip code, we run it like this
        if (location.length() == 5 && m.matches() == true) {
            //we have a zip code only and will add this to the web url
            finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(location.trim());
            Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
            String urlToUse = buildURL.build().toString();
            handleURL(urlToUse);
        }

        if(splited.length==1 && m.matches()== false){
            finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(location.trim());
            Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
            String urlToUse = buildURL.build().toString();
            handleURL(urlToUse);
        }
        //if we get a city, state, and zip we run this




        if (splited.length == 3 && splited[2].length() ==5) {
            Matcher m2 = p.matcher(splited[2]);
            if(m2.matches()==true) {
                finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(splited[2].trim());
                Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
                String urlToUse = buildURL.build().toString();
                handleURL(urlToUse); }

        }
        else if (splited.length == 3) {
            Matcher m2 = p.matcher(splited[2]);
            if(m2.matches()==true) {
                String city = splited[0];
                String city2 = splited[1].substring(0, splited[1].length()-1);
                city = city.concat(city2);
                finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(city.trim());
                Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
                String urlToUse = buildURL.build().toString();
                handleURL(urlToUse); }

        }

        if (splited.length == 2 && splited[0].charAt(splited[0].length()-1)==',') {
            String city = splited[0].substring(0, splited[0].length()-1);
            city = city.substring(0,1).toUpperCase() + city.substring(1);
            finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(city.trim());
            Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
            String urlToUse = buildURL.build().toString();
            handleURL(urlToUse);
        }

        else if(splited.length == 2 && splited[0].charAt(splited[0].length()-1)!=',') {
            String city = splited[0].concat(splited[1].trim());
            finalURL = DATA_URL.concat(yourAPIKey).concat(connector.trim()).concat(city.trim());
            Uri.Builder buildURL = Uri.parse(finalURL).buildUpon();
            String urlToUse = buildURL.build().toString();
            handleURL(urlToUse);
        }
    }

    public void handleURL(String url) {
        StringBuilder sb = new StringBuilder();

        try {
            URL urlToUse = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlToUse.openConnection();
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.noLocation();
                }
            });

        }

        try {
            handleResults(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleResults(String s) throws JSONException {
        HashMap<Integer, String> officialMap = new HashMap<>();
        JSONObject jObjMain = new JSONObject(s);
        JSONArray jOfficial = jObjMain.getJSONArray("offices");
        for (int i = 0; i < jOfficial.length(); i++) {
            JSONObject jOffice = (JSONObject) jOfficial.get(i);
            String title = jOffice.getString("name");
            //System.out.println("The title is " + title);
            JSONArray jIndices = jOffice.getJSONArray("officialIndices");
            String index = jIndices.get(0).toString();
            if (title.equals("U.S. Senator")) {
                officialMap.put(Integer.parseInt(jIndices.get(0).toString()), title);
                officialMap.put(Integer.parseInt(jIndices.get(1).toString()), title);
            } else {
                officialMap.put(Integer.parseInt(jIndices.get(0).toString()), title);
            }
        }
        getInfo(officialMap, s);
        Iterator hmIterator = officialMap.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            //System.out.println(mapElement.getKey() + " : " + mapElement.getValue());
        }



    }

    public void getInfo (HashMap map, String s) throws JSONException {
        ArrayList<Official> officialList = new ArrayList<>();

        JSONObject jObjMain = new JSONObject(s);
        JSONArray jOfficial = jObjMain.getJSONArray("officials");
        //System.out.println("The size is " + jOfficial.length());
        //we will iterate through the array and get the objects one by one to add to the list
        for(int i = 0 ; i<jOfficial.length(); i++) {
            Official official = new Official("", "", "", "", "",
            "", "", "", "", "", "");
            JSONObject jOfficer = (JSONObject) jOfficial.get(i);
            String name = jOfficer.getString("name");
            official.setName(name);
            if (jOfficer.has("address")) {
                JSONArray jAddress = jOfficer.getJSONArray("address");
                JSONObject address = (JSONObject) jAddress.get(0);
                String street = address.getString("line1");
                String city = address.getString("city");
                String state = address.getString("state");
                String zip = address.getString("zip");
                String finalAddress = street.concat(" " + city).concat(", " + state).concat(" " + zip);
                official.setAddress(finalAddress);
                //System.out.println(finalAddress);
            }

            String party = jOfficer.getString("party");
            official.setParty(party);

            if (jOfficer.has("phones")) {
                JSONArray jPhone = jOfficer.getJSONArray("phones");
                String phone = jPhone.getString(0);
                official.setPhone(phone);
            }

            if (jOfficer.has("urls")) {
                JSONArray jURL = jOfficer.getJSONArray("urls");
                String url = jURL.getString(0);
                official.setWebsite(url);

            }

            if (jOfficer.has("emails")) {
                JSONArray jEmail = jOfficer.getJSONArray("emails");
                String email = jEmail.getString(0);
                official.setEmail(email);
                //System.out.println(email);
            }

            if(jOfficer.has("photoUrl")) {
                String photo = jOfficer.getString("photoUrl");
                System.out.println("The photo url is " + photo);
                official.setPhotoURL(photo);
            }

            if(jOfficer.has("channels")) {
                JSONArray socials = jOfficer.getJSONArray("channels");
                String twit;
                String fb;
                String yt;
                if(socials.length()==3){
                    JSONObject social1 = socials.getJSONObject(0);
                    JSONObject social2 = socials.getJSONObject(1);
                    JSONObject social3 = socials.getJSONObject(2);
                    fb = social1.getString("id");
                    twit = social2.getString("id");
                    yt = social3.getString("id");
                    official.setFaceURL(fb);
                    official.setTwitURL(twit);
                    official.setYtURL(yt);
                }
                if(socials.length()==2){
                    JSONObject social1 = socials.getJSONObject(0);
                    JSONObject social2 = socials.getJSONObject(1);
                    if(social1.getString("type").equals("Facebook")){
                        fb=social1.getString("id");
                        official.setFaceURL(fb);
                    }
                    else if (social1.getString("type").equals("Twitter")){
                        twit=social1.getString("id");
                        official.setTwitURL(twit);
                    }

                    if(social2.getString("type").equals("Facebook")){
                        fb=social2.getString("id");
                        official.setFaceURL(fb);
                    }
                    else if (social2.getString("type").equals("Twitter")){
                        twit=social2.getString("id");
                        official.setTwitURL(twit);
                    }
                    else if (social2.getString("type").equals("YouTube")){
                        yt=social2.getString("id");
                        official.setYtURL(yt);
                    }
                }

                if(socials.length()==1){
                    JSONObject social1 = socials.getJSONObject(0);
                    if(social1.getString("type").equals("Facebook")){
                        fb=social1.getString("id");
                        official.setFaceURL(fb);
                    }
                    else if (social1.getString("type").equals("Twitter")){
                        twit=social1.getString("id");
                        official.setTwitURL(twit);
                    }
                    else if (social1.getString("type").equals("YouTube")){
                        yt=social1.getString("id");
                        official.setYtURL(yt);
                    }
                }


            }

            String title = (String) map.get(i);
            official.setTitle(title);

            officialList.add(official);


        }


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.hello(officialList);
            }
        });








    }
}
