package com.example.es.util.readSetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ReadAccountMessage {

    ReadJsonFile readJsonFile;

    private ReadJsonFile getReadJsonFile() {
        if (readJsonFile == null) {
           return  readJsonFile = new ReadJsonFile();
        }
        return readJsonFile;
    }

    public String readPathConfig()  {

        // so, the first step is getting corresponding path that contains the account and password
        // base on the security

        String path = Objects.requireNonNull(ReadAccountMessage.class.getClassLoader()
                .getResource("privateInformation/account.json")).getPath();

        return getReadJsonFile().readJsonSetting(path);
    }

    public String getAccountPathInfo() {

        String accountJsonString = readPathConfig();

        JSONObject jsonObject = JSON.parseObject(accountJsonString);

        return (String) jsonObject.getJSONObject("privateMessage").get("accountPath");

    }

    public HashMap<String, String> getAccountInformation() {
        HashMap<String, String> hashMap = new HashMap<>();

        String infoPath = getAccountPathInfo();

        String realInfoString = getReadJsonFile().readJsonSetting(infoPath);

        JSONObject jsonObject = JSON.parseObject(realInfoString);

        String account  = (String) jsonObject.getJSONObject("ray").get("account");
        String password = (String) jsonObject.getJSONObject("ray").get("password");

        hashMap.put("username", account);
        hashMap.put("password", password);

        return hashMap;
    }

}
