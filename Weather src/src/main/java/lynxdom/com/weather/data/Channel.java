package lynxdom.com.weather.data;

import org.json.JSONObject;

public class Channel implements JSONPopulator {
    private Units unit;

    public Units getUnit() {
        return unit;
    }

    public Item getItem() {
        return item;
    }

    private Item item;
    @Override
    public void populate(JSONObject data) {
        unit = new Units();
        unit.populate(data.optJSONObject("units"));
        item = new Item();
        item.populate(data.optJSONObject("item"));
    }
}
