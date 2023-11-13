package cn.iocoder.springboot.lab03.kafkademo.consumer;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.config.KafkaConfiguration;
import cn.iocoder.springboot.lab03.kafkademo.producer.ResponseProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
@Component
public class SimulationConsumer {
    @Autowired
    private RedisTemplate redisTemplate;

    private AtomicInteger count = new AtomicInteger(0);

    @Value("#{${spring.server.unrealMap}}")
    private Map<String, String> unrealMap; //用一个map来存unreal程序的port和对应的路径

    private Logger logger = LoggerFactory.getLogger(getClass());
    private int num = 1;
    @Value("${spring.pixel-streaming.host}")
    private String PixelStreamingIP;
    @Autowired
    private ResponseProducer responseProducer = new ResponseProducer();

    @KafkaListener(topics = KafkaConfiguration.SEND_TOPIC,
            groupId = "request-consumer-group-" + KafkaConfiguration.SEND_TOPIC)
    public void onMessage(String record) throws ExecutionException, InterruptedException, IOException {

        JSONObject jsonObject = JSONUtil.parseObj(record);
        redisTemplate.opsForList().rightPush("kafkaQueue", jsonObject);

        logger.info("收到simulation request");
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
        startUnreal: for(;;){
            for(Map.Entry<String, String> entry : unrealMap.entrySet()){
                if (!redisTemplate.hasKey(entry.getKey())){
                    //把port存入redis
                    redisTemplate.opsForValue().set(entry.getKey(), entry.getValue(),110, TimeUnit.SECONDS);
                    //打开指定的shell
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec(entry.getValue() + " -AudioMixer -PixelStreamingIP="+ PixelStreamingIP+ " -PixelStreamingPort=" + entry.getKey());
                    logger.info("start port application"+ entry.getValue() + " -AudioMixer -PixelStreamingIP="+ PixelStreamingIP+ " -PixelStreamingPort=" + entry.getKey());

                    break startUnreal;
                }
            }

            Thread.currentThread().sleep(3000);

        }

    }

}
