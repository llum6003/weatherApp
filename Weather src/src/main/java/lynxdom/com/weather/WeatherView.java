package lynxdom.com.weather;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lynxdom.com.weather.data.Channel;
import lynxdom.com.weather.services.WeatherServiceCallback;
import lynxdom.com.weather.services.YahooWeatherServices;

public class WeatherView extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherImageView;
    private TextView tempTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private ProgressDialog dialog;

    private YahooWeatherServices services;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_view);
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        services = new YahooWeatherServices(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading info..");
        dialog.show();
        services.refreshYahooWeatherServices("Austin, TX");
    }

    @Override
    public void serviceSuccess(Channel ch) {
        dialog.hide();

       int resourceId = getResources().getIdentifier("drawable/w" + ch.getItem().getCondition().getCode(), null, getPackageName());
        Drawable weatherIcon = getResources().getDrawable(resourceId, null);
        weatherImageView.setImageDrawable(weatherIcon.getCurrent());
        locationTextView.setText(services.getLocation());
        tempTextView.setText(ch.getItem().getCondition().getTemperature() + "\u00B0 " + ch.getUnit().getTemperature());
        conditionTextView.setText(ch.getItem().getCondition().getDesc());
    }

    @Override
    public void serviceFailure(Exception ex) {
        dialog.hide();
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
