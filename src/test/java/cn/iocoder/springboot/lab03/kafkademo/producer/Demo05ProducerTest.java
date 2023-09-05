package cn.iocoder.springboot.lab03.kafkademo.producer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Demo05ProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Demo05Producer producer;

    @Test
    public void testSyncSend5() throws ExecutionException, InterruptedException {
//        String jsonMessage = "{\"equivalent\":1,\"eventLon\":1,\"sustainTime\":11,\"dataType\":2,\"ip\":\"127.0.0.1\",\"bombType\":1,\"eventType\":2,\"diffusionRange\":[\"10\",\"50\"],\"port\":9092,\"createTime\":1691573546507,\"geography\":\"0\",\"eventLat\":1,\"eventTime\":\"2023-08-10 12:00:00\",\"topic\":\"effect-data-out-topic\",\"id\":\"1689208002922356737\",\"diffusionTime\":[\"1\",\"2\"]}";
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("a", "value1");
        jsonObject.put("b", "value2");
        jsonObject.put("c", "value3");
        System.out.println(jsonObject);
        String jsonMessage = jsonObject.toString();
        System.out.println(jsonMessage);
        SendResult result = producer.syncSend(jsonObject);
        logger.info("[testSyncSend][发送json内容：[{}] 发送结果：[{}]]", jsonMessage, result);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void testASyncSend5() throws InterruptedException {
//        String jsonMessage = "{\"equivalent\":1,\"eventLon\":1,\"sustainTime\":11,\"dataType\":2,\"ip\":\"127.0.0.1\",\"bombType\":1,\"eventType\":2,\"diffusionRange\":[\"10\",\"50\"],\"port\":9092,\"createTime\":1691573546507,\"geography\":\"0\",\"eventLat\":1,\"eventTime\":\"2023-08-10 12:00:00\",\"topic\":\"effect-data-out-topic\",\"id\":\"1689208002922356737\",\"diffusionTime\":[\"1\",\"2\"]}";
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("id",1);
        jsonObject.put("msg", "test json");
        String jsonMessage = jsonObject.toString();


        producer.asyncSend(jsonMessage).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

            @Override
            public void onFailure(Throwable e) {
                logger.info("[testASyncSend][发送编号：[{}] 发送异常]]", jsonMessage, e);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                logger.info("[testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", jsonMessage, result);
            }

        });

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

//    @Test
//    public void testSyncSendX() throws ExecutionException, InterruptedException {
//        for (int i = 0; i < 100; i++) {
//            int id = (int) (System.currentTimeMillis() / 1000);
//            SendResult result = producer.syncSend(id);
//            logger.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);
//            Thread.sleep(10 * 1000L);
//        }
//
//        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
//    }

}
