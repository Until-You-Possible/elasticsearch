package com.example.es.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

import static com.example.es.core.Constants.*;

@RestController
@RequestMapping("/py")
public class PythonTestController {

    @GetMapping("/index")
    public HashMap<String, Object> index() {
        HashMap<String, Object> hashMap = new HashMap<>();
        String fileName = "pythonFile/KNN.py";
        Process process;
        try {
            String pyFilePath = Objects.requireNonNull(PythonTestController.class.getClassLoader()
                    .getResource(fileName)).getPath();
            String command = PYTHON3 + " "  + pyFilePath;
            String[] args = new String[] { PYTHON3, pyFilePath,};
            process = Runtime.getRuntime().exec(args);
            // 用输入输出流来获取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int  ch = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((ch = in.read()) !=-1 ) {
                stringBuilder.append((char) ch);
            }
            in.close();
            hashMap.put(MESSAGE, "test python");
            hashMap.put(RESULT, stringBuilder.toString());
            hashMap.put(FILEPATH, pyFilePath);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

}
