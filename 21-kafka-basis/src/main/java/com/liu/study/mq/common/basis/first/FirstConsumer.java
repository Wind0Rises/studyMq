package com.liu.study.mq.common.basis.first;


import com.liu.study.mq.common.kafka.common.CommonConstant;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @desc 客户端
 *       【注意远程连接的时候，把对应的端口打开，centos系统使用firewall-cmd --zone=public --add-port=9092/tcp --permanent】
 *
 *       消费者具体有哪些配置可以查看：ConsumerConfig
 *
 * @author Liuweian
 * @createTime 2020/6/18 22:22
 * @version 1.0.0
 */
public class FirstConsumer {

    private Consumer<String, String> consumer;

    public FirstConsumer() {
        /**
         * 01、创建Kafka的生产者：
         *      * bootstrap.servers：指定了Kafka集群的连接字符串。
         *      * group.id：指定消费者属于哪个一起群组。
         */
        Properties properties = new Properties();
        // properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("bootstrap.servers", "192.168.20.160:9092");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "1");
        consumer = new KafkaConsumer<String, String>(properties);
    }

    public void traditionConsume() {
        consumer.subscribe(Collections.singleton(CommonConstant.BASIS_FIRST_TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord item : records) {
                String logger = String.format("Topic: %s，partition：$s，key：%s，value：%s", item.topic(), item.partition(), item.key(), item.value());
                System.out.println(logger);
            }
        }
    }

    /**
     * 手动同步提交。
     *
     * 手动提交有一个不足之处，在 broker 对提交请求作出回应之前，应用程序会一直阻塞，这样会限制应用程序的吞吐量。我们可以通过降低提交频率来提升吞吐盆，
     * 但如果发生了再均衡， 会增加重复消息的数量。
     */
    public void commitSyncConsume() {
        consumer.subscribe(Collections.singleton(CommonConstant.BASIS_FIRST_TOPIC));

        while (true) {
            // 在poll(0)中consumer会一直阻塞直到它成功获取了所需的元数据信息，之后它才会发起fetch请求去获取数据。虽然poll可以指定超时时间，
            // 但这个超时时间只适用于后面的消息获取，前面更新元数据信息不计入这个超时时间。poll(Duration)这个版本修改了这样的设计，会把元数
            // 据获取也计入整个超时时间。由于本例中使用的是0，即瞬时超时，因此consumer根本无法在这么短的时间内连接上coordinator，所以只能赶
            // 在超时前返回一个空集合。这就是为什么使用不同版本的poll命令assignment不同的原因。
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord item : records) {
                String logger = String.format("Topic: %s，partition：$s，key：%s，value：%s", item.topic(), item.partition(), item.key(), item.value());
                System.out.println(logger);
            }
            consumer.commitSync();
        }
    }

    /**
     * 手动异步提交。
     *
     *
     */
    public void commitAsyncConsume() {
        consumer.subscribe(Collections.singleton(CommonConstant.BASIS_FIRST_TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord item : records) {
                String logger = String.format("Topic: %s，partition：$s，key：%s，value：%s", item.topic(), item.partition(), item.key(), item.value());
                System.out.println(logger);
            }
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {

                }
            });
        }
    }


    public static void main(String[] args) {
        FirstConsumer consumer = new FirstConsumer();
        consumer.traditionConsume();
    }

}
