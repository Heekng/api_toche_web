package com.heekng.api_toche_web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fnv1aHash {

    public static String hashCDragonValue(String text) {
        String lowerText = text.toLowerCase();
        int hash = 0x811c9dc5;

        for (int i = 0; i < lowerText.length(); i++) {
            hash ^= lowerText.charAt(i);
            hash *= 0x1000193;
        }

        String hashString = Integer.toHexString(hash);

        int hashStringSize = hashString.length();
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("{");
        for (int i = 0; i < 8 - hashStringSize; i++) {
            resultBuilder.append("0");
        }
        resultBuilder.append(hashString);
        resultBuilder.append("}");
        return resultBuilder.toString();
    }

    public static Map<String, String> getTextAndHashTextMapByTexts(List<String> texts) {
        Map<String, String> hashMap = new HashMap<>();
        for (String text : texts) {
            String hashString = hashCDragonValue(text);
            hashMap.put(text, hashString);
        }
        return hashMap;
    }
}
