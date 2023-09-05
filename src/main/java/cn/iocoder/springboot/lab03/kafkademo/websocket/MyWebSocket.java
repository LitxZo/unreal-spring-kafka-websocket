package cn.iocoder.springboot.lab03.kafkademo.websocket;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.springboot.lab03.kafkademo.producer.ResponseProducer;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
@Component
@ServerEndpoint(path = "/", port = "8098")
public class MyWebSocket {

//    WebSocketServerHandshakerFactory wsFactory =
//            new WebSocketServerHandshakerFactory(uri,WebSocketVersion.V13, null,true,new DefaultHttpHeaders(), 65536*5);

    @Value("${spring.server.host}")
    private String serverIp;
    @Autowired
    private RedisTemplate redisTemplate;
//    public static Queue<JSONObject> KafkaData = new LinkedList<>();
    @Autowired
    private ResponseProducer responseProducer;
    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        session.setSubprotocols("stomp");
//        if (!"ok".equals(req)){
//            System.out.println("Authentication failed!");
//            session.close();
//        }
    }
    
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        System.out.println("new connection");
//        System.out.println(req);
        String msg = redisTemplate.opsForList().leftPop("kafkaQueue").toString();

        JSONObject jsonObject = JSONUtil.parseObj(msg);
        JSONObject responseObject = JSONUtil.createObj();
        responseObject.put("taskId", jsonObject.getStr("taskId"));
        responseObject.put("fogIntensity", jsonObject.getFloat("fogIntensity"));
        responseObject.put("time", jsonObject.getStr("time")); // enum 白天，黄昏，夜晚
        responseObject.put("cloud", jsonObject.getBool("cloud"));
        responseObject.put("explosionLevel", jsonObject.getInt("explosionLevel"));
        responseObject.put("inAir", jsonObject.getBool("inAir"));
        responseObject.put("lighting", jsonObject.getBool("lighting"));
        JSONObject windObj = jsonObject.getJSONObject("wind");
        JSONObject respWindObj = JSONUtil.createObj();
        respWindObj.put("x", windObj.getDouble("x"));
        respWindObj.put("y", windObj.getDouble("y"));
        respWindObj.put("z", windObj.getDouble("z"));
        responseObject.put("wind", respWindObj);
        responseObject.put("levelName", jsonObject.getStr("levelName"));
        session.sendText(responseObject.toString());
//        session.sendText("{\"taskId\":\"0000000000\",\"fogIntensity\":0.006,\"weather\":\"1\",\"time\":\"1\",\"cloud\":true,\"explosionLevel\":3,\"wind\":{\"x\":0,\"y\":0,\"z\":\n" +
//                "0},\"levelName\":\"IslandMap\"}");
        System.out.println("send kafkaMsg to UE");
        System.out.println("msg: " + responseObject.toString());


    }

    @OnClose
    public void onClose(Session session) throws IOException {

       System.out.println("one connection closed"); 
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) throws ExecutionException, InterruptedException {
        System.out.println("message: " + message);
        JSONObject jsonObject = JSONUtil.parseObj(message);
        JSONObject responseObject = JSONUtil.createObj();
        String status = jsonObject.getStr("status");
//        boolean exit = jsonObject.getBool("exit");
        if (Objects.equals(status, "OK")){
            System.out.println("status: " + status);
            responseObject.put("isUrl", 1);
            responseObject.put("taskId", jsonObject.getStr("taskId"));
            responseObject.put("url",serverIp + ":" + jsonObject.getStr("port"));
            responseProducer.syncSend(responseObject);

//            String msg = redisTemplate.opsForList().leftPop("kafkaQueue").toString();
//
//            JSONObject redisObj = JSONUtil.parseObj(msg);
//            JSONObject unrealResponse = JSONUtil.createObj();
//            unrealResponse.put("taskId", redisObj.getStr("taskId"));
//            unrealResponse.put("fogIntensity", redisObj.getFloat("fogIntensity"));
//            unrealResponse.put("time", redisObj.getStr("time")); // enum 白天，黄昏，夜晚
//            unrealResponse.put("cloud", redisObj.getBool("cloud"));
//            unrealResponse.put("explosionLevel", redisObj.getInt("explosionLevel"));
//            unrealResponse.put("inAir", redisObj.getBool("inAir"));
//            unrealResponse.put("lighting", redisObj.getBool("lighting"));
//            JSONObject windObj = redisObj.getJSONObject("wind");
//            JSONObject respWindObj = JSONUtil.createObj();
//            respWindObj.put("x", windObj.getDouble("x"));
//            respWindObj.put("y", windObj.getDouble("y"));
//            respWindObj.put("z", windObj.getDouble("z"));
//            unrealResponse.put("wind", respWindObj);
//            unrealResponse.put("levelName", redisObj.getStr("levelName"));
//            session.sendText(unrealResponse.toString());

        }
        else if (Objects.equals(status, "Done")) {
            System.out.println("status: " + status);
            responseObject.put("isUrl", 0);
            responseObject.put("taskId", jsonObject.getStr("taskId"));
            responseObject.put("impactRadius", jsonObject.getDouble("impactRadius"));
            responseObject.put("freq", jsonObject.getJSONArray("freq"));
            responseObject.put("timeList", jsonObject.getJSONArray("timeList"));
            responseObject.put("explosionRadius", jsonObject.getDouble("explosionRadius"));
            SendResult rep =  responseProducer.syncSend(jsonObject);
            System.out.printf(rep.toString());

        }
        else{
            System.out.println(jsonObject.toString());
            //从redis中删除对应的应用
            redisTemplate.delete("88" + jsonObject.getInt("port").toString());

        }


    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes); 
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

}