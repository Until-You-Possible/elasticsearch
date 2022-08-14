package com.example.es.util;

import com.alibaba.fastjson.JSONObject;
import com.example.es.util.readSetting.ReadAccountMessage;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.es.core.Constants.DATA;

public class CommonUtil {

    public static String getType(Object o) {
        return o.getClass().toString();
    }

    public static List<Object> toParseArray(JSONObject json) {
        String getString = json.get(DATA).toString();
        return JSONObject.parseArray(getString, Object.class);
    }

    public static<T> List<T> mergeArray(List<T> list1, List<T> list2, List<T> list3) {
        return Stream.of(list1, list2, list3).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static String getResourceFilePath (String pathName) {
        return Objects.requireNonNull(ReadAccountMessage.class.getClassLoader()
                .getResource(pathName)).getPath();
    }


}
