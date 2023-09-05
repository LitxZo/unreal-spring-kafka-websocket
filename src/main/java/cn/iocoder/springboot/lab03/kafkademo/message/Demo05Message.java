package cn.iocoder.springboot.lab03.kafkademo.message;

public class Demo05Message {

    public static final String TOPIC = "DEMO_06";
    private String jsonMessage;

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public  String getJsonMessage() {
        return jsonMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "jsonMessage=" + jsonMessage +
                '}';
    }
}
