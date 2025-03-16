package json.types;

public final class JSONFloat implements JSONObject<Float> {
    private float value;

    public JSONFloat(float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }
}
