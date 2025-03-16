package json.types;

public final class JSONInteger implements JSONObject<Integer> {
    private int value;

    public JSONInteger(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
