package com.londonappbrewery.climapm;

import android.util.Log;

import org.json.JSONObject;

public class WeatherDataModel extends WeatherController {
    private String mTemperature;
    private int mCondition;
    private String mCity;
    private String mIconName;



    public WeatherDataModel() {
    }

    public WeatherDataModel(String temperature, int condition, String city, String iconName) {
        mTemperature = temperature;
        mCondition = condition;

        mCity = city;
        mIconName = iconName;

    }



    public static WeatherDataModel fromJson(JSONObject jsonObject){
       try {
           WeatherDataModel weatherDataModel=new WeatherDataModel(
                   String.valueOf(Math.rint(jsonObject.getJSONObject("main").getDouble("temp")-273)),

                   jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id"),


                   jsonObject.getString("name"),
                   updateWeatherIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id"))








                           );
           return weatherDataModel;
       }catch (Exception e){

           e.printStackTrace();

return null;
       }






    }



    // TODO: Declare the member variables here


    // TODO: Create a WeatherDataModel from a JSON:


    // TODO: Uncomment to this to get the weather image name from the condition:
    public static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // TODO: Create getter methods for temperature, city, and icon name:

    public String getTemperature() {
        return mTemperature + "°C";


    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return mIconName;
    }

    public int getCondition() {
        return mCondition;
    }
}
