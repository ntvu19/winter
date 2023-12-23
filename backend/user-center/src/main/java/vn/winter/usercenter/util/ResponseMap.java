package vn.winter.usercenter.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseMap {
    private final Map<String, Object> responseMap;

    public ResponseMap() {
        responseMap = new HashMap<>();
    }

    public ResponseMap setValue(String key, Object value) {
        this.responseMap.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        // Clone the map
        Map<String, Object> cloneMap = new HashMap<>(this.responseMap);

        // Clear old information
        responseMap.clear();

        // Return the clone map
        return cloneMap;
    }
}
