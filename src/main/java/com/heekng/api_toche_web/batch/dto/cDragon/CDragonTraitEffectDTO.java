package com.heekng.api_toche_web.batch.dto.cDragon;

import com.heekng.api_toche_web.util.Fnv1aHash;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CDragonTraitEffectDTO {

    private Integer maxUnits;
    private Integer minUnits;
    private Integer style;
    private Map<String, Float> variables;

    public void updateVariablesToHash() {
        Map<String, Float> hashVariablesMap = new HashMap<>();
        for (String variableName : variables.keySet()) {
            Float variableValue = variables.get(variableName);
            if (variableName.contains("{")) {
                hashVariablesMap.put(variableName, variableValue);
            } else {
                String hashVariableName = Fnv1aHash.hashCDragonValue(variableName);
                hashVariablesMap.put(hashVariableName, variableValue);
            }
        }
        variables = hashVariablesMap;
    }
}
