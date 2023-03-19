package com.example.myfinalproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void openMap(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:61.5027,23.7538" +
                "?q=Ilmarinkatu+44,+33500+Tampere,+Finland+(K-Market+Ilmarinkatu)"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No map service
        }
    }

    public void openTuni(View view) {
        // Open www.tuni.fi
        String urlString = "https://www.tuni.fi";
        Uri uri = Uri.parse(urlString);
        // Create the implicit intent
        // Implicit intent == the sys will choose the activity to be used
        // Explicit intent == the developer choose the activity, e.g. MySecondActivity.class
        Intent openWebPage = new Intent(Intent.ACTION_VIEW, uri);

        // require QUERY_ALL_PACKAGES
//        if (openWebPage.resolveActivity(getPackageManager())!=null){
//            ...
//        }
        // so we do this instead...
        try {
            startActivity(openWebPage);
        } catch (ActivityNotFoundException e) {
            // there is no viewer for web pages (http)
        }
    }

    public void openForecastActivity(View view) {
        // switch to the other screen and send a msg to it
        Intent openForecast = new Intent(this, ForecastActivity.class);
        openForecast.putExtra("CITY_NAME", "Tampere");
        startActivity(openForecast);

    }
}