package com.example.es.controller;


import com.example.es.util.readSetting.ReadLocalFile;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;


@RestController
@RequestMapping("/pmml")
public class PmmlTestController {

    private static ReadLocalFile readJsonFile;

    private static ReadLocalFile getReadJsonFile() {
        if (readJsonFile == null) {
            return  readJsonFile = new ReadLocalFile();
        }
        return readJsonFile;
    }

    @GetMapping("/index")
    public void pm() throws IOException {
        HashMap<String, ?> hashMap = new HashMap<>();
        Evaluator model = this.loadPMML();
        predict(model);
    }

    public Evaluator loadPMML() throws IOException {
        String fileName = "PMML/DecisionTreeIris.pmml";
        String filePath = Objects.requireNonNull(PmmlTestController.class.getClassLoader().getResource(fileName)).getPath();
        InputStream inputStream = null;
        File file = new File(filePath);
        PMML pmml = new PMML();
        try {
            inputStream = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(inputStream == null){
            return null;
        }
        InputStream is = inputStream;
        try {
            pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
        } catch (SAXException | JAXBException e1) {
            e1.printStackTrace();
        } finally {
            //关闭输入流
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        return modelEvaluatorFactory.newModelEvaluator(pmml);
    }


    private void predict(Evaluator evaluator) {
        // 测试数据
        Map<String, Double> map = new HashMap<>();
        map.put("x1", 1.1);
        map.put("x2", 2.5);
        map.put("x3", 5.4);
        map.put("x4", 3.2);

        List<InputField> inputFields = evaluator.getInputFields();
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputFieldName = inputField.getName();
            Object rawValue = map.get(inputFieldName.getValue());
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }

        Map<FieldName, ?> results = evaluator.evaluate(arguments);
        List<TargetField> targetFields = evaluator.getTargetFields();

        for (TargetField targetField : targetFields) {
            FieldName targetFieldName = targetField.getName();
            Object targetFieldValue = results.get(targetFieldName);
            System.out.println(targetFieldValue);
        }

    }

}
