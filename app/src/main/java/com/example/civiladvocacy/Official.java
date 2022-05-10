package com.example.civiladvocacy;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

public class Official {
    private String address;
    private String phone;
    private String email;
    private String website;
    private String party;
    private String name;
    private String title;
    private String photoURL;
    private String faceURL;
    private String twitURL;
    private String ytURL;

    public Official(String name, String address, String party, String phone, String website, String email, String title
        , String photoURL, String faceURL, String twitURL, String ytURL) {
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.party = party;
        this.name = name;
        this.title = title;
        this.photoURL=photoURL;
        this.faceURL = faceURL;
        this.twitURL = twitURL;
        this.ytURL = ytURL;
    }

    public void setAddress(String cool) {
        address=cool;
    }
    public void setPhotoURL(String cool) {photoURL=cool; }
    public void setPhone(String cool) {
        phone=cool;
    }
    public void setEmail(String cool) {
        email=cool;
    }
    public void setWebsite(String cool) {
        website=cool;
    }
    public void setParty(String cool) {
        party=cool;
    }
    public void setName(String cool) {
        name=cool;
    }
    public void setTitle(String cool) {
        title=cool;
    }

    public void setFaceURL(String faceURL) {
        this.faceURL = faceURL;
    }

    public void setTwitURL(String twitURL) {
        this.twitURL = twitURL;
    }

    public void setYtURL(String ytURL) {
        this.ytURL = ytURL;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getParty() {
        return party;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoURL() {return photoURL; }

    public String getFaceURL() {
        return faceURL;
    }

    public String getTwitURL() {
        return twitURL;
    }

    public String getYtURL() {
        return ytURL;
    }

    public String toString() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("address").value(getAddress());
            jsonWriter.name("phone").value(getPhone());
            jsonWriter.name("email").value(getEmail());
            jsonWriter.name("website").value(getWebsite());
            jsonWriter.name("party").value(getParty());
            jsonWriter.name("name").value(getName());
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("photourl").value(getPhotoURL());
            jsonWriter.name("faceurl").value(getFaceURL());
            jsonWriter.name("twiturl").value(getTwitURL());
            jsonWriter.name("yturl").value(getYtURL());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();



        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return "";
    }


}
