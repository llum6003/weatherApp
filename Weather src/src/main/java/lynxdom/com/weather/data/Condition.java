package lynxdom.com.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Condition implements JSONPopulator {
    private int code;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDesc() {
        return desc;
    }

    private int temperature;
    private String desc;
    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        try {
            desc = data.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
