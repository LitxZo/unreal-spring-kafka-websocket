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

//
//      String jsonMessage = "{\"equivalent\":1,\"eventLon\":1,\"sustainTime\":11,\"dataType\":2,\"ip\":\"127.0.0.1\",\"bombType\":1,\"eventType\":2,\"diffusionRange\":[\"10\",\"50\"],\"port\":9092,\"createTime\":1691573546507,\"geography\":\"0\",\"eventLat\":1,\"eventTime\":\"2023-08-10 12:00:00\",\"topic\":\"effect-data-out-topic\",\"id\":\"1689208002922356737\",\"diffusionTime\":[\"1\",\"2\"]}";
//        for(int i = 0; i < 10; i++){
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
        logger.info("[testSyncSend][发送json内容：[{}] 发送结果：[{}]]", jsonObject, result);
//        }
//
//        JSONObject jsonObject2 = JSONUtil.createObj();
//        jsonObject2.put("taskId", "12");
//        jsonObject2.put("fogIntensity", 0.006);
////        jsonObject2.put("weather", "1");
//        jsonObject2.put("time", "黄昏"); // enum 白天，黄昏，夜晚
//        jsonObject2.put("cloud", true);
//        jsonObject2.put("explosionLevel", 3);
//        jsonObject2.put("lighting", false);
//        jsonObject2.put("inAir", true);
//        JSONObject windObj2 = JSONUtil.createObj();
//        windObj2.put("x", 0.0);
//        windObj2.put("y", 0.0);
//        windObj2.put("z", 0.0);
//        jsonObject2.put("wind", windObj2);
////        jsonObject2.put("levelName", "IslandMap");
//        jsonObject2.put("levelName", "PlainMap");
//        System.out.println(jsonObject2);
//        SendResult result2 = producer.syncSend(jsonObject2);
//        logger.info("[testSyncSend][发送json内容：[{}] 发送结果：[{}]]", jsonObject2, result2);

//        JSONObject jsonObject3 = JSONUtil.createObj();
//        jsonObject2.put("taskId", "12");
//        jsonObject2.put("fogIntensity", 0.006);
////        jsonObject2.put("weather", "1");
//        jsonObject2.put("time", "黄昏"); // enum 白天，黄昏，夜晚
//        jsonObject2.put("cloud", true);
//        jsonObject2.put("explosionLevel", 3);
//        jsonObject2.put("lighting", false);
//        jsonObject2.put("inAir", true);
//        JSONObject windObj3 = JSONUtil.createObj();
//        windObj2.put("x", 0.0);
//        windObj2.put("y", 0.0);
//        windObj2.put("z", 0.0);
//        jsonObject2.put("wind", windObj3);
////        jsonObject2.put("levelName", "IslandMap");
//        jsonObject2.put("levelName", "PlainMap");
//        System.out.println(jsonObject2);
//        SendResult result3 = producer.syncSend(jsonObject2);
//        logger.info("[testSyncSend][发送json内容：[{}] 发送结果：[{}]]", jsonObject2, result2);


        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
