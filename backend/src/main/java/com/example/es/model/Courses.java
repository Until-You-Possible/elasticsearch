package com.example.es.model;

import com.alibaba.fastjson.JSONObject;

import java.time.Instant;
import java.util.HashMap;

public class Courses {

    public static final String DATA = "data";

    public static final String ID = "id";

    public static final String COURSE_ID = "courseId";

    public static final String NAME = "name";

    public static final String COURSE_CODE = "courseCode";

    public static final String ABSTRACT = "abstract";

    public static final String DESCRIPTION = "description";

    public static final String LANGUAGES = "languages";

    public static final String START_DATE = "startDate";

    public static final String END_DATE = "endDate";

    public static final String IMAGE = "image";

    public static final String VIDEO = "video";

    public static final String LEARNING_OBJECTIVES = "learningObjectives";

    public static final String DURATION = "duration";

    public static final String ATTRIBUTES = "attributes";

    public static final String URL = "url";

    public static final String TIMESTAMP = "__timestamp";

    // response pagination info
    public static final String LINKS = "links";

    public static final String LAST = "last";

    public static final String PAGE = "page";

    public static HashMap<String, Object> formatterSingleCourse(JSONObject jsonObject) {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        // differ from _id in elasticsearch
        objectHashMap.put(Courses.COURSE_ID, getStringValue(jsonObject, Courses.ID));
        JSONObject attributes = jsonObject.getJSONObject(Courses.ATTRIBUTES);
        objectHashMap.put(Courses.NAME, getStringValue(attributes, Courses.NAME));
        objectHashMap.put(Courses.COURSE_CODE, getStringValue(attributes, Courses.COURSE_CODE));
        objectHashMap.put(Courses.ABSTRACT, getStringValue(attributes, Courses.ABSTRACT));
        objectHashMap.put(Courses.DESCRIPTION, getStringValue(attributes, Courses.DESCRIPTION));
        objectHashMap.put(Courses.LANGUAGES, attributes.getJSONArray(Courses.LANGUAGES));
        objectHashMap.put(Courses.START_DATE, getStringValue(attributes, Courses.START_DATE));
        objectHashMap.put(Courses.END_DATE, getStringValue(attributes, Courses.END_DATE));
        objectHashMap.put(Courses.IMAGE, getStringValue(attributes.getJSONObject(Courses.IMAGE), Courses.URL));
        objectHashMap.put(Courses.VIDEO, getStringValue(attributes.getJSONObject(Courses.VIDEO), Courses.URL));
        objectHashMap.put(Courses.LEARNING_OBJECTIVES, attributes.getJSONArray(Courses.LEARNING_OBJECTIVES));
        objectHashMap.put(Courses.DURATION, getStringValue(attributes, Courses.DURATION));
        objectHashMap.put(Courses.TIMESTAMP, Instant.now().toString());
        return objectHashMap;
    }

    static String getStringValue(JSONObject target, String key) {
        return target == null ? "" : target.containsKey(key) ? target.getString(key) : "";
    }
}
