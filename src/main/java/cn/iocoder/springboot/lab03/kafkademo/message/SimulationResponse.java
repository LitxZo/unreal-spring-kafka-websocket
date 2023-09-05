package cn.iocoder.springboot.lab03.kafkademo.message;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

public class SimulationResponse {
    // 配置response topic
    public static final String TOPIC = "effect-data-topic-2";
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
