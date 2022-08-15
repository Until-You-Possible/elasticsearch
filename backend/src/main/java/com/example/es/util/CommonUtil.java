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


    /**
     * 说明：在没有打注解@SafeVarargs之前 会有waring： Possible heap pollution from parameterized vararg type
     *  heap pollution(堆污染)
     *  在java编程语言中，当一个可变generic参数指向一个无generic时， head pollution就有可能发生
     *  head pollution 可能导致 ClassCastException 等异常。
     *  可变的非具体化 形式参数的方法消除警告
     *  1：使用注解： @SafeVarargs
     *  2：使用注解：@SuppressWarnings({"unchecked", "varargs"})
     *
     * @param lists lists
     * @param Object <T>
     * @return list<T>
     */
    @SafeVarargs
    public static<T> List<T> mergeArray(List<T> ...lists) {
        return Stream.of(lists).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static String getResourceFilePath (String pathName) {
        return Objects.requireNonNull(ReadAccountMessage.class.getClassLoader()
                .getResource(pathName)).getPath();
    }


}
