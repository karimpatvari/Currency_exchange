package Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PatchHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String body;
    private final Map<String, String[]> parameterMap;

    public PatchHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        body = sb.toString();
        parameterMap = parseBody();
    }

    @Override
    public String getParameter(String name) {
        String[] params = parameterMap.get(name);
        return (params != null && params.length > 0) ? params[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return new HashMap<>(parameterMap);
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }

    private Map<String, String[]> parseBody() {
        Map<String, String[]> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                map.merge(key, new String[]{value}, (oldValues, newValues) -> {
                    String[] mergedValues = new String[oldValues.length + 1];
                    System.arraycopy(oldValues, 0, mergedValues, 0, oldValues.length);
                    mergedValues[oldValues.length] = newValues[0];
                    return mergedValues;
                });
            }
        }
        return map;
    }
}