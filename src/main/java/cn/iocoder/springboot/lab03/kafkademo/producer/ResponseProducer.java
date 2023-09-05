package cn.iocoder.springboot.lab03.kafkademo.producer;

import cn.hutool.json.JSONObject;
import cn.iocoder.springboot.lab03.kafkademo.message.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Component
public class ResponseProducer {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult syncSend(JSONObject jsonMessage) throws ExecutionException, InterruptedException {

        //ProducerRecord<Object, String> producerRecord = new ProducerRecord<>("DEMO_05", jsonMessage);
        // 同步发送消息
        return kafkaTemplate.send(SimulationResponse.TOPIC, jsonMessage).get();
    }

}
