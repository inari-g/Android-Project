package com.example.myfinalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastActivity extends AppCompatActivity {

    private RequestQueue queue;
    private double temperature;
    private String description = "Click to refresh";
    private double windSpeed;
    private String image;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Create a new web request queue
        queue = Volley.newRequestQueue(this);

        // the screen is opening here
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("CITY_NAME");

        TextView forecastHeaderTextView = findViewById(R.id.cityTextView);
        if (cityName != null) {
            forecastHeaderTextView.setText(cityName);
        } else {
            forecastHeaderTextView.setText("Location not known.");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    // Save the ui state here: description, temperature, wind and weather icon
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("WEATHER_DESCRIPTION", description);
        outState.putDouble("TEMPERATURE", temperature);
        outState.putDouble("WIND", windSpeed);
        outState.putString("IMAGE",image);
        outState.putString("IMAGE_URL",imageUrl);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Check if there is a bundle and activity ui data saved
        // If so, read the values from there
        description = savedInstanceState.getString("WEATHER_DESCRIPTION");
        if (description == null) {
            description = "Click to refresh";
        }
        temperature = savedInstanceState.getDouble("TEMPERATURE", 0);
        windSpeed = savedInstanceState.getDouble("WIND", 0);
        image = savedInstanceState.getString("IMAGE");

        // Write the values on the UI
        TextView descriptionTextView = findViewById(R.id.descripTextView);
        descriptionTextView.setText(description);
        TextView temperatureTextView = findViewById(R.id.tempTextView);
        temperatureTextView.setText("" + temperature + "C");
        TextView windTextView = findViewById(R.id.windTextView);
        windTextView.setText("" + windSpeed + "m/s");
        ImageView imageView = findViewById(R.id.imageView);
        imageUrl = "http://openweathermap.org/img/wn/" + image + "@2x.png";
        Picasso.get().load(imageUrl).into(imageView);
    }

    public void fetchWeatherData(View view) {
        // Make the request here and put it in the queue to get the response
        String url = "https://api.openweathermap.org/data/2.5/weather?q=tampere&units=metric&" +
                "appid=6c433438776b5be4ac86001dc88de74d";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // The response is here as a string
                    Log.d("WEATHER_APP", response);
                    parseJsonAndUpdateUI(response);
                }, error -> {
            // Error (timeout, other errors)
            Log.d("WEATHER_APP", error.toString());
        });

        // Add the request to the queue to actually send it
        queue.add(stringRequest);
    }

    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject weatherResponse = new JSONObject(response);

            temperature = weatherResponse.getJSONObject("main").getDouble("temp");
            windSpeed = weatherResponse.getJSONObject("wind").getDouble("speed");
            description = weatherResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            image = weatherResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            imageUrl = "http://openweathermap.org/img/wn/" + image + "@2x.png";

            // Write the values on the UI
            TextView descriptionTextView = findViewById(R.id.descripTextView);
            descriptionTextView.setText(description);
            TextView temperatureTextView = findViewById(R.id.tempTextView);
            temperatureTextView.setText("" + temperature + "C");
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText("" + windSpeed + "m/s");
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(imageUrl).into(imageView);


        } catch (JSONException e) {
            // if we got some problems with JSON parsing
            e.printStackTrace();
        }
    }
}