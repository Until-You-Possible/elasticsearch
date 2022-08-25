package com.example.es.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.es.util.readSetting.ReadAccountMessage;
import com.example.es.util.readSetting.ReadLocalFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.es.core.Constants.*;

public class CommonUtil {

    private static ReadLocalFile readJsonFile;

    private static ReadLocalFile getReadJsonFile() {
        if (readJsonFile == null) {
            return  readJsonFile = new ReadLocalFile();
        }
        return readJsonFile;
    }

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

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    // 获取数据源(目前存放本地的json)
    public static HashMap<String, List<Object>> getIndexJsonArray(String indexName) {
        HashMap<String, List<Object>> hashMap = new HashMap<>();
        // 读取json存放的路径
        String resourceConfigPath = getReadJsonFile().readJsonSetting(CommonUtil.getResourceFilePath(PRIVATERESOURCEPATH));
        JSONObject jsonObject = JSONObject.parseObject(resourceConfigPath);
        String jsonPath = (String) jsonObject.getJSONObject("privateMessage").get(indexName);
        // 读取对应的文件
        JSONObject jsonFile = JSONObject.parseObject(getReadJsonFile().readJsonSetting(jsonPath));
        JSONArray jsonArray = jsonFile.getJSONObject(HITS).getJSONArray(HITS);
        hashMap.put(RESULT, jsonArray.toJavaList(Object.class));
        return hashMap;
    }


}
