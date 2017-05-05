package com.example.muneikh.utility;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLParser {
    private static final String TAG = "URLParser";

    private static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static String getParamFromURL(@NonNull String url, @NonNull String param) {
        String value = null;
        try {
            Map<String, String> params = URLParser.splitQuery(new URL(url));
            value = params.get(param);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getParamFromURL: " + e.getLocalizedMessage());
        } catch (MalformedURLException e) {
            Log.e(TAG, "getParamFromURL: " + e.getLocalizedMessage());
        }
        return value;
    }
}
