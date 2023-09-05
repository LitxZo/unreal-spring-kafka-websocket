package cn.iocoder.springboot.lab03.kafkademo.producer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.Application;
import cn.iocoder.springboot.lab03.kafkademo.websocket.MyWebSocket;
import jdk.nashorn.api.scripting.JSObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Demo04ProducerTest {

//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private Demo04Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
//        int id = (int) (System.currentTimeMillis() / 1000);
//        SendResult result = producer.syncSend(id);
//        logger.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);
//
//        // 阻塞等待，保证消费
//        new CountDownLatch(1).await();
//        System.out.println(MyWebSocket.KafkaData);
//        JSONObject jsonObject = JSONUtil.createObj();
//        jsonObject.put("a", "1");
//        MyWebSocket.KafkaData.add(jsonObject);
//        System.out.println(MyWebSocket.KafkaData);
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
