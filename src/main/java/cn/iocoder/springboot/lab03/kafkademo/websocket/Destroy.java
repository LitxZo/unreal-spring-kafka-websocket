package cn.iocoder.springboot.lab03.kafkademo.websocket;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Destroy implements DisposableBean {

    @Value("#{${spring.server.unrealMap}}")
    private Map<String, String> unrealMap;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void destroy() throws Exception {
        for(Map.Entry<String, String> entry : unrealMap.entrySet()){
            if (redisTemplate.hasKey(entry.getKey())){
                redisTemplate.delete(entry.getKey());
            }
        }
        if (redisTemplate.hasKey("kafkaQueue")){
            redisTemplate.delete("kafkaQueue");
        }
        System.out.println("---------redis clean----------");
    }
}
