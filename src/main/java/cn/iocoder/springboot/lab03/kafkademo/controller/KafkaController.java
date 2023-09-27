package cn.iocoder.springboot.lab03.kafkademo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.producer.ResponseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class KafkaController {
    @Autowired
    private ResponseProducer responseProducer;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/video/{taskid}")
    public void postVideo(@PathVariable String taskid, @RequestParam MultipartFile blobFile) throws ExecutionException, InterruptedException, IOException {
        System.out.println("post video:");
        System.out.println(taskid);

        byte[] fileBytes = blobFile.getBytes();
        String base64Data = Base64.getEncoder().encodeToString(fileBytes);
        JSONObject responseObject = JSONUtil.createObj();
        responseObject.put("taskId", taskid);
        responseObject.put("videoBlob", base64Data);

        redisTemplate.opsForList().rightPush("videoQueue", responseObject);
        JSONObject kafkaResponse = JSONUtil.createObj();
        kafkaResponse.put("taskId", taskid);
        kafkaResponse.put("type", "video");
        responseProducer.syncSend(kafkaResponse);
    }
}