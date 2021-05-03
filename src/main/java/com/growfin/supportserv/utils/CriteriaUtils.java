package com.growfin.supportserv.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CriteriaUtils {

    public static boolean isQueryParamAvailable(Map<String, List<String>> queryParams, String key) {
        return Objects.nonNull(queryParams) && queryParams.containsKey(key) && queryParams.get(key).size() > 0;
    }

    public static boolean isQueryValueAvailable(List<String> valueList, String value) {
        return Objects.nonNull(valueList) && valueList.stream().anyMatch(eachValue->eachValue.equalsIgnoreCase(value));
    }

}
