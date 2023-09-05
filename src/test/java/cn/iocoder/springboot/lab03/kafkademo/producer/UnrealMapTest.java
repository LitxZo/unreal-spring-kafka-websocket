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

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UnrealMapTest {

    @Value("#{${spring.server.unrealMap}}")
    private Map<String, String> unrealMap;

    @Value("${spring.server.host}")
    private String PixelStreamingPort;
    @Test
    public void test() throws ExecutionException, InterruptedException, IOException {

        System.out.println(unrealMap);
        System.out.println(unrealMap.get("8880"));
        Runtime runtime = Runtime.getRuntime();
        runtime.exec(unrealMap.get("8880") + " -AudioMixer -PixelStreamingIP="+ PixelStreamingPort+ " -PixelStreamingPort=" + "8880");
//
        new CountDownLatch(1).await();
    }

}
