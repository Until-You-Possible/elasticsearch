package com.example.es.controller;


import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/pmml2")
public class PmmlTestOtherController {

    public static Evaluator evaluator;

    @GetMapping("/index")
    public void pm() throws JAXBException, IOException, SAXException {
        HashMap<String, ?> hashMap = new HashMap<>();
        // Evaluator model = this.loadPMML();
        // predict(model);
        this.initModel();
    }

    public void initModel() throws JAXBException, IOException, SAXException {
        String filePath = "/Users/c5312072/Desktop/github/es/elasticsearch/backend/src/main/resources/PMML/DecisionTreeIris.pmml";
        File file = new File(filePath);
        evaluator = new LoadingModelEvaluatorBuilder().load(file).build();
        evaluator.verify();
        System.out.println("XXXXX" + evaluator);
    }

}
