package cn.iocoder.springboot.lab03.kafkademo.consumer;

import cn.iocoder.springboot.lab03.kafkademo.config.KafkaConfiguration;
import cn.iocoder.springboot.lab03.kafkademo.message.SimulationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ResponseConsumer {


    private AtomicInteger count = new AtomicInteger(0);

    private Logger logger = LoggerFactory.getLogger(getClass());


    @KafkaListener(topics = KafkaConfiguration.RESPONSE_TOPIC,
            groupId = "simulation-consumer-group-" + KafkaConfiguration.RESPONSE_TOPIC)
    public void onMessage(String record) {
        logger.info("收到simulation response");
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);

        System.out.println("收到引擎处理数据" + record);

    }

}
