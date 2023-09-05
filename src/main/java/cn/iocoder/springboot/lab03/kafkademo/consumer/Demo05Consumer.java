package cn.iocoder.springboot.lab03.kafkademo.consumer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.message.Demo04Message;
import cn.iocoder.springboot.lab03.kafkademo.message.Demo05Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Demo05Consumer {

    private AtomicInteger count = new AtomicInteger(0);

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = "DEMO_05",
            groupId = "demo05-consumer-group-" +"DEMO_05")
    public void onMessage(String record) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
        System.out.println(record);
        JSONObject jsonObject = JSONUtil.parseObj(record);
        System.out.println(jsonObject);
        // 注意，此处抛出一个 RuntimeException 异常，模拟消费失败
//        throw new RuntimeException("我就是故意抛出一个异常");
    }

}
