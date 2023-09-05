package cn.iocoder.springboot.lab03.kafkademo.message;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulationMessage {
    // 配置request topic
    public static final String TOPIC = "topic-1";
    private JSONObject jsonObject;

    public void setJsonMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public  JSONObject getJsonMessage() {
        return jsonObject;
    }
    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
