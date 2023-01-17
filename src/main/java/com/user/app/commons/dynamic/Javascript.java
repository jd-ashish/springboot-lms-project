package com.user.app.commons.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Javascript {

    public static List<String> getScripts(String... scripts) {
        List<String> result = new ArrayList<>();
        Collections.addAll(result, scripts);
//        result.add("javascript ");
        return result;
    }
}
