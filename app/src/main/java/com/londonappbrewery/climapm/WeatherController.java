package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.mode;
import static android.R.attr.targetClass;
import static com.londonappbrewery.climapm.WeatherDataModel.updateWeatherIcon;


public class WeatherController extends AppCompatActivity {

    // Constants:
    final int RequestCode=1;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // TODO: Set LOCATION_PROVIDER here:
    String mLocationProvider = LocationManager.GPS_PROVIDER;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    TextView weather;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        weather=(TextView)findViewById(R.id.weather);
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);

        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WeatherController.this,ChangeCityController.class);
                startActivity(intent);
            }
        });


        // TODO: Add an OnClickListener to the changeCityButton here:

    }


    // TODO: Add onResume() here:

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
    
        String city=intent.getStringExtra("City");

        Toast.makeText(WeatherController.this, city, Toast.LENGTH_SHORT).show();

        if(city!=null){
            getWeatherForNewCity(city);

        }else {
            getWeatherForCurrentLocation();
        }
    }



    // TODO: Add getWeatherForNewCity(String city) here:
    public void getWeatherForNewCity(String city){
        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        letsDoSomeNetworking(params);


    }


    // TODO: Add getWeatherForCurrentLocation() here:

    protected void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("onLocationChanged", "onLocationChanged is called");
                String Longitide=String.valueOf(location.getLongitude());
                String Latitude=String.valueOf(location.getLatitude());

                Log.e("Longitude and Latitude","Longitude = "+ Longitide +" Latitude = "+ Latitude);


                RequestParams params=new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitide);
                params.put("appid",APP_ID);

                letsDoSomeNetworking(params);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("onProviderEnabled", "onProviderEnabled is called");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("onProviderDisabled", "onProviderDisabled is called");


            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},RequestCode);

            return;
        }
        mLocationManager.requestLocationUpdates(mLocationProvider, MIN_TIME, MIN_DISTANCE, mLocationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==RequestCode){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.e("Permission","Permission Granted");
                getWeatherForCurrentLocation();
            }else {
                Log.e("Per","Permission Denied");
            }
        }
    }
    // TODO: Add letsDoSomeNetworking(RequestParams params) here:

    public void letsDoSomeNetworking(RequestParams params){

        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);




                WeatherDataModel weatherDataModel=WeatherDataModel.fromJson(response);




                updateUI(weatherDataModel);
               weather.setText(WeatherDataModel.updateWeatherIcon(weatherDataModel.getCondition()));


                Log.i("Success",response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.i("Fail",throwable.toString() + statusCode);

                Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }



    // TODO: Add updateUI() here:

    private void updateUI(WeatherDataModel model){
      mCityLabel.setText(model.getCity());
        mTemperatureLabel.setText(model.getTemperature());

        int resourceId=getResources().getIdentifier(model.getIconName(),"drawable",getPackageName());
        System.out.println(model.getIconName());
        System.out.println(resourceId + "Mujahid");
        mWeatherImage.setImageResource(resourceId);




    }



    // TODO: Add onPause() here:


    @Override
    protected void onPause() {
        super.onPause();

        if(mLocationManager!=null){

            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}




