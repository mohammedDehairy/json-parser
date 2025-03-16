package json.types;

import java.util.Objects;

public final class JSONString implements JSONObject<String> {
    private String value;

    public JSONString(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
