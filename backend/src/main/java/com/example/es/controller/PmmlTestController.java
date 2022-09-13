package com.example.es.controller;


import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/pmml")
public class PmmlTestController {

    @GetMapping("/index")
    public void pm() {

    }

    public Evaluator loadPMML() throws IOException {
        String fileName = "PMML/DecisionTreeIris.pmml";
        String filePath = Objects.requireNonNull(PmmlTestController.class.getClassLoader().getResource(fileName)).getPath();
        PMML pmml = new PMML();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(inputStream == null){
            return null;
        }
        InputStream is = inputStream;
        try {
            pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
        } catch (SAXException | ParserConfigurationException e1) {
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

    private  int predict(Evaluator evaluator) {
        // 测试数据
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("x1", 1.1);
        map.put("x2", 2.5);
        map.put("x3", 5.4);
        map.put("x4", 3.2);

        List<InputField> inputFields = evaluator.getInputFields();
        Map<FieldNameSet, FieldValue> arguments = new LinkedHashMap<FieldNameSet, FieldValue>();
        for (InputField inputField : inputFields) {
            String inputFieldName = inputField.getName();
            Object rawValue = data.get
        }

    }

}
