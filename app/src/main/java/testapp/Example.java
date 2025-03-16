package testapp;

import java.util.List;

public class Example {
    private String stringKey;
    private Integer intKey;
    private Float floatKey;
    private Boolean falseKey;
    private Boolean trueKey;
    private List<Example> nestedArray;
    private Example nestedObject;

    public String getStringKey() {
        return stringKey;
    }

    public void setStringKey(String stringKey) {
        this.stringKey = stringKey;
    }

    public Integer getIntKey() {
        return intKey;
    }

    public void setIntKey(Integer intKey) {
        this.intKey = intKey;
    }

    public Float getFloatKey() {
        return floatKey;
    }

    public void setFloatKey(Float floatKey) {
        this.floatKey = floatKey;
    }

    public Boolean getFalseKey() {
        return falseKey;
    }

    public void setFalseKey(Boolean falseKey) {
        this.falseKey = falseKey;
    }

    public Boolean getTrueKey() {
        return trueKey;
    }

    public void setTrueKey(Boolean trueKey) {
        this.trueKey = trueKey;
    }

    public List<Example> getNestedArray() {
        return nestedArray;
    }

    public void setNestedArray(List<Example> nestedArray) {
        this.nestedArray = nestedArray;
    }

    public Example getNestedObject() {
        return nestedObject;
    }
    
    public void setNestedObject(Example nestedObject) {
        this.nestedObject = nestedObject;
    }

    @Override
    public String toString() {
        return "{\n" +
            "stringKey='" + getStringKey() + "',\n" +
            "intKey='" + getIntKey() + "',\n" +
            "floatKey='" + getFloatKey() + "',\n" +
            "falseKey='" + getFalseKey() + "',\n" +
            "trueKey='" + getTrueKey() + "',\n" +
            "nestedArray='" + getNestedArray() + "',\n" +
            "nestedObject='" + getNestedObject() + "'\n" +
            "}";
    }
    
    
}
