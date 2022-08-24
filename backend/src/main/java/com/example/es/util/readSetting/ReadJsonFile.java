package com.example.es.util.readSetting;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReadJsonFile {

    public String readJsonSetting(String fileName) {

        String jsonString = "";

        try {

            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);

            int  ch = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((ch = reader.read()) !=-1 ) {
                stringBuilder.append((char) ch);
            }

            fileReader.close();
            reader.close();
            jsonString = stringBuilder.toString();
            return jsonString;

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        }
    }
}
