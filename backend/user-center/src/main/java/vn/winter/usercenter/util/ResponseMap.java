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

    public ResponseMap setMessage(String message) {
        return setValue("message", message);
    }

    public ResponseMap setStatus(int status) {
        return setValue("status", status);
    }

    public ResponseMap setData(Object data) {
        return setValue("data", data);
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
