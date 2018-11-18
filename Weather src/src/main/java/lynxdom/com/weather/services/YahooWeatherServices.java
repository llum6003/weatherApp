package lynxdom.com.weather.services;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import lynxdom.com.weather.data.Channel;

public class YahooWeatherServices {
    private WeatherServiceCallback callback;
    private Exception error;

    public String getLocation() {
        return location;
    }

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }




    public YahooWeatherServices(WeatherServiceCallback callback) {
        this.callback = callback;
    }


    public void refreshYahooWeatherServices(String location) {
        this.location = location;
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", strings[0]);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream in = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder results = new StringBuilder();
                    String line = "";
                    while ((line=br.readLine())!=null) {
                        results.append(line);
                    }
                    //doWork(results.toString());
                    return results.toString();
                } catch (MalformedURLException e) {
                    error = e;
                } catch (IOException e) {
                    error = e;
                }
                return null;
            }


            @Override
            protected void onPostExecute(String s) {
                if (s==null && error!=null) {
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);
                    JSONObject resultQuery = data.optJSONObject("query");
                    int count = resultQuery.optInt("count");
                    if (count==0) {
                        callback.serviceFailure(new LocalWeatherException("City Not Found"));
                    }
                    Channel ch = new Channel();
                    ch.populate(resultQuery.optJSONObject("results").optJSONObject("channel"));
                    callback.serviceSuccess(ch);
                } catch (JSONException e) {
                    callback.serviceFailure(e);
                    e.printStackTrace();
                }

            }
        }.execute(location);


    }
}

class LocalWeatherException extends Exception {

    public LocalWeatherException(String message) {
        super(message);
    }
}