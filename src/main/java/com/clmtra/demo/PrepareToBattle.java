package com.clmtra.demo;

import com.clmtra.demo.domain.Warrior;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.camunda.spin.Spin.JSON;

@Component
public class PrepareToBattle implements JavaDelegate {

    @Value("${maxWarriors}")
    private int maxWarriors;

    @Value("${url}")
    private String url;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        int warriors = (int) delegateExecution.getVariable("warriors");
        int enemyWarriors = (int) (Math.random() * 100);

        maxWarriors = maxWarriors == 0 ? 100 : maxWarriors;

        if (warriors < 1 || warriors > maxWarriors) {
            throw new BpmnError("warriorsError");
        }

        List<Warrior> army = new ArrayList<>();
        for (int i = 0; i <= warriors; i++) {
            army.add(createWarrior());
        }

        System.out.println("Prepare to battle! Enemy army = " + enemyWarriors + " vs. our army: " + warriors);

        ObjectValue armyJson = Variables.objectValue(army).serializationDataFormat("application/json").create();
        delegateExecution.setVariable("army", army);
        delegateExecution.setVariable("armyJson", armyJson);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);

    }

    private Warrior createWarrior() {
        Warrior warrior = new Warrior();

        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest request = httpConnector.createRequest().get().url(url);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        request.setRequestParameters(headers);

        HttpResponse response = request.execute();

        if (response.getStatusCode() == 200) {
            warrior = JSON(response.getResponse()).mapTo(Warrior.class);
//            SpinJsonNode node = Spin.JSON(response.getResponse());
//            warrior.setName(node.prop("name.findName").stringValue());
//            warrior.setTitle(node.prop("name.title").stringValue());
//            warrior.setHp(Integer.parseInt(node.prop("random.number").stringValue()));
            warrior.setAlive(true);
        }
        response.close();

        return warrior;
    }
}
