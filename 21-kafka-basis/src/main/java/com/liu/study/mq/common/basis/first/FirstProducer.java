package com.liu.study.mq.common.basis.first;

import com.liu.study.mq.common.kafka.common.CommonConstant;
import com.liu.study.mq.common.kafka.common.MessageUtils;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * @desc  kafka生产者。
 *        【注意远程连接的时候，把对应的端口打开，centos系统使用firewall-cmd --zone=public --add-port=9092/tcp --permanent】
 *
 *        生产者具体有哪些配置，可以查看：CommonClientConfigs
 * @author Liuweian
 * @createTime 2020/6/18 22:23
 * @version 1.0.0
 */
public class FirstProducer {

    private final Producer<String, String> producer;

    public FirstProducer() {
        /**
         * 01、创建Kafka的生产者：
         *      bootstrap.servers：该属性指定broker的地址清单，地址的格式为host:port。
         *      key.serializer：broker希望接收到的消息的键和值都是字节数组。key.deserializer必须被设置为一个实现了。org.apache.kafka.
         *          common.serialization.Serializer接口的类，生产者会使用这个类把键对象序列化成字节数组。
         *      value.serialize：与key.serializer类似。
         */
        Properties properties = new Properties();
        // properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("bootstrap.servers", "192.168.20.160:9092");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<String, String>(properties);

        /**
         * 消息发送的三种方式：
         *      1： 发送并忘记(fire-and-forget)
         *          我们把消息发送给服务器，但井不关心它是否正常到达。大多数情况下，消息会正常到达，因为Kafka是高可用的，而且生产者会自动尝试重发。
         *          不过，使用这种方式有时候也会丢失一些消息。
         *
         *      2、并发发送
         *          我们使用send()方怯发送消息，它会返回一个Future对象，调用get()方法进行等待，就可以知道悄息是否发送成功。
         *
         *      3、异步发送
         *          我们调用send()方怯，并指定一个回调函数， 服务器在返回响应时调用该函数。
         */
    }

    /**
     * 发送并忘记
     */
    public void produceForFireAndForget() {
        // 01、构建ProducerRecord。
        String key = MessageUtils.getMessage("Fire And Forget");
        String data = "【Fire And Forget】 kafka message " + key;
        ProducerRecord record = new ProducerRecord<String, String>(CommonConstant.BASIS_FIRST_TOPIC, key ,data);

        // 02、使用producer发送Record
        System.out.println("======= 【Fire And Forget】发生开始   key: " + key + "   ===============" );
        producer.send(record);
        System.out.println("======= 【Fire And Forget】发生成功   key: " + key + "   ===============" );
    }


    public void produceForSyn() {
        // 01、构建ProducerRecord。
        String key = MessageUtils.getMessage("Syn");
        String data = "【Syn】 kafka message " + key;
        ProducerRecord record = new ProducerRecord<String, String>(CommonConstant.BASIS_FIRST_TOPIC, key ,data);

        // 02、发送并获取返回
        try {
            System.out.println("=======  【Syn】发生开始   key: " + key + "   ===============" );
            producer.send(record).get();
            System.out.println("=======  【Syn】发生成功   key: " + key + "   ===============" );
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void produceForAsync() {
        // 01、构建ProducerRecord。
        String key = MessageUtils.getMessage("Async");
        String data = "【Async】 kafka message " + key;
        ProducerRecord record = new ProducerRecord<String, String>(CommonConstant.BASIS_FIRST_TOPIC, key ,data);

        System.out.println("=======  【Async】发生开始   key: " + key + "   ===============" );
        producer.send(record, new SendCallback());
        System.out.println("=======  【Async】发生成功   key: " + key + "   ===============" );
    }

    private static class SendCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FirstProducer producer = new FirstProducer();
        producer.produceForFireAndForget();
        producer.produceForSyn();
        producer.produceForAsync();
    }

}
