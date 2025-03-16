package json.types;

public final class JSONBoolean implements JSONObject<Boolean> {
    private boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
