package cn.iocoder.springboot.lab03.kafkademo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.producer.ResponseProducer;
import cn.iocoder.springboot.lab03.kafkademo.producer.SimulationProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
public class SendMsgController {
    @Autowired
    private SimulationProducer producer;
    @Autowired
    private ResponseProducer responseProducer;

    @RequestMapping("/send")
    public void sendMsg() throws ExecutionException, InterruptedException {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("taskId", "12");
        jsonObject.put("fogIntensity", 0.006);

        jsonObject.put("time", "黄昏"); // enum 白天，黄昏，夜晚
        jsonObject.put("cloud", true);
        jsonObject.put("explosionLevel", 3);
        jsonObject.put("lighting", false);
        jsonObject.put("inAir", false);
        JSONObject windObj = JSONUtil.createObj();
        windObj.put("x", 0.0);
        windObj.put("y", 0.0);
        windObj.put("z", 0.0);
        jsonObject.put("wind", windObj);
//        jsonObject.put("levelName", "IslandMap");
        jsonObject.put("levelName", "PlainMap");

        System.out.println(jsonObject);

        SendResult result = producer.syncSend(jsonObject);
    }
}