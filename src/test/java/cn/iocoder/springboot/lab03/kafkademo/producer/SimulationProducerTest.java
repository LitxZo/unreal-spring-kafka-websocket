package cn.iocoder.springboot.lab03.kafkademo.producer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SimulationProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SimulationProducer producer;

    @Test
    public void testSimulationSyncSend() throws ExecutionException, InterruptedException {


        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("taskId", "12");
        jsonObject.put("fogIntensity", 0.006);
        jsonObject.put("weather", "1");
        jsonObject.put("time", "黄昏"); // enum 白天，黄昏，夜晚
        jsonObject.put("cloud", true);
        jsonObject.put("equivalent", 3);
        jsonObject.put("lighting", false);
        jsonObject.put("geography", 1);
        JSONObject windObj = JSONUtil.createObj();
        windObj.put("x", 0);
        windObj.put("y", 0);
        windObj.put("z", 0);
        jsonObject.put("wind", windObj);
        jsonObject.put("bombType", 1);
        jsonObject.put("eventType", 1);
        jsonObject.put("eventLon", 100);
        jsonObject.put("eventLat", 100);
        jsonObject.put("sustainTime", 11);
        jsonObject.put("dataType", 2);
        jsonObject.put("windSpeed", 100);
        jsonObject.put("windTrend", "东");
        String[] diffusionRange = {"10", "50"};
        String[] diffusionTime = {"1", "2"};
        jsonObject.put("diffusionRange", diffusionRange);
        jsonObject.put("diffusionTime", diffusionTime);
        System.out.println(jsonObject);
        SendResult result = producer.syncSend(jsonObject);
        logger.info("[testSyncSend][发送json内容：[{}] 发送结果：[{}]]", jsonObject, result);




        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
