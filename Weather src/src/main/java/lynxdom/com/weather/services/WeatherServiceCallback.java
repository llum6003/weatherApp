package lynxdom.com.weather.services;

import lynxdom.com.weather.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel ch);
    void serviceFailure(Exception ex);
}
