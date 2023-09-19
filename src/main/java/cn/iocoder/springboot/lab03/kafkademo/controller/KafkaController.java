package cn.iocoder.springboot.lab03.kafkademo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.producer.ResponseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class KafkaController {
    @Autowired
    private ResponseProducer responseProducer;

    @PostMapping("/video/{taskid}")
    public String postVideo(@PathVariable String taskid, @RequestBody String s) throws ExecutionException, InterruptedException {
        System.out.println("post video:");
        System.out.println(taskid);
        JSONObject responseObject = JSONUtil.createObj();
        responseObject.put("taskId", taskid);
        responseObject.put("videoBlob", s);
        responseProducer.syncSend(responseObject);
        return s;
    }
}