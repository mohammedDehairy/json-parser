package json.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JSONArray implements JSONObject<List<JSONObject<?>>> {
    private List<JSONObject<?>> values;

    public JSONArray() {
        this.values = new ArrayList<>();
    }

    public void add(JSONObject<?> jsonObject) {
        Objects.requireNonNull(jsonObject);
        values.add(jsonObject);
    }

    public List<JSONObject<?>> getValue() {
        return values;
    }
}
