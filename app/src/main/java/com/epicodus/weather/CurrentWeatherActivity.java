package com.epicodus.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.epicodus.weather.adapters.WeatherListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CurrentWeatherActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private WeatherListAdapter mAdapter;
    public ArrayList<String> descriptionArray =  new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_weather);
        ButterKnife.bind(this);
        getWeather("London,uk");
    }

    private void getWeather(String location) {
        final WeatherService weatherService = new WeatherService();
        weatherService.getForecast(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();

//                  for today:  String weatherJSON = new JSONObject(jsonData).getJSONArray("weather").getJSONObject(0).getString("description");



                    JSONArray weatherJSON = new JSONObject(jsonData).getJSONArray("list");
                    for (int i=0; i < weatherJSON.length(); i++) {
                       String desc =  weatherJSON.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                        descriptionArray.add(desc);
                    }
                    Log.v(TAG, String.valueOf(descriptionArray));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new WeatherListAdapter(getApplicationContext(), descriptionArray);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(CurrentWeatherActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);

                    }
                });
            }
        });
    }
}
