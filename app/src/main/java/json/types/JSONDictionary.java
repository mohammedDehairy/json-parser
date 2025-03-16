package json.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JSONDictionary implements JSONObject<Map<String, JSONObject<?>>> {
    private Map<String, JSONObject<?>> dictionary;

    public JSONDictionary() {
        this.dictionary = new HashMap<>();
    }

    public void put(String key, JSONObject<?> value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        dictionary.put(key, value);
    }

    public Map<String, JSONObject<?>> getValue() {
        return dictionary;
    }
}
