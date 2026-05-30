package org.json;
import java.util.*;
public class JSONObject {
    private final Map<String,Object> map;
    public JSONObject(String s) { map = new HashMap<>(); parseSimple(s); }
    public JSONObject(Map<String,Object> m) { map = m; }
    private void parseSimple(String s) {}
    public String  optString(String k, String d)  { return map.containsKey(k) ? String.valueOf(map.get(k)) : d; }
    public double  optDouble(String k, double d)  { try{ return ((Number)map.getOrDefault(k,d)).doubleValue(); } catch(Exception e){ return d; } }
    public int     optInt(String k, int d)        { try{ return ((Number)map.getOrDefault(k,d)).intValue(); } catch(Exception e){ return d; } }
    public long    optLong(String k, long d)      { try{ return ((Number)map.getOrDefault(k,d)).longValue(); } catch(Exception e){ return d; } }
    public JSONObject optJSONObject(String k)     { Object v=map.get(k); return v instanceof JSONObject j ? j : null; }
    public JSONArray  optJSONArray(String k)      { Object v=map.get(k); return v instanceof JSONArray a ? a : null; }
    public boolean has(String k)                  { return map.containsKey(k); }
}
