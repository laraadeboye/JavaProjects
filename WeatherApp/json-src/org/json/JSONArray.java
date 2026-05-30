package org.json;
import java.util.*;
public class JSONArray {
    private final List<Object> list = new ArrayList<>();
    public int length() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public JSONObject getJSONObject(int i) { return (JSONObject) list.get(i); }
    public Object get(int i) { return list.get(i); }
}
