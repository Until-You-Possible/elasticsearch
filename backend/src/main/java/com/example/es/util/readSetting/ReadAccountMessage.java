package com.example.es.util.readSetting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.es.util.CommonUtil;
import java.util.HashMap;

import static com.example.es.core.Constants.*;


public class ReadAccountMessage {

    ReadLocalFile readJsonFile;

    private ReadLocalFile getReadJsonFile() {
        if (readJsonFile == null) {
           return  readJsonFile = new ReadLocalFile();
        }
        return readJsonFile;
    }

    public String readPathConfig()  {

        // so, the first step is getting corresponding path that contains the account and password
        // base on the security
        // privateInformation/account.json

        String path = CommonUtil.getResourceFilePath("privateInformation/account.json");

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

        String account  = (String) jsonObject.getJSONObject("ray").get(ACCOUNT);
        String password = (String) jsonObject.getJSONObject("ray").get(PASSWORD);

        hashMap.put(USERNAME, account);
        hashMap.put(PASSWORD, password);

        return hashMap;
    }

}
