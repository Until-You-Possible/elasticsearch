package com.example.es.core;

public enum EnumIndexesType {

    SYSTEM("system", "system_setting.json"),
    COURSES("courses","courses_setting.json"),
    USER("user","user_setting.json"),
    PRODUCT("product", "product_setting.json");

    private final String name;

    private final String settingName;

    EnumIndexesType(String name, String settingName) {
        this.name = name;
        this.settingName = settingName;
    }

    public String getName() {
        return name;
    }

    public String getSettingName() {
        return settingName;
    }

}
