package json.types;

public sealed interface JSONObject<Value> permits JSONArray, JSONBoolean, JSONDictionary, JSONFloat, JSONInteger, JSONString {
    Value getValue();
}
