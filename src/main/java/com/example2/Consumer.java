package com.example2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Consumer.class);

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // группа (как минимум) определяет offset (он свой у каждой группы)
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group7");

        // --- OFFSET settings ---
        //
        // The offset is a simple integer number that is used by Kafka to
        // maintain the current position of a consumer. The current offset
        // is a pointer to the last record that Kafka has already sent to
        // a consumer in the most recent poll. So, the consumer doesn't
        // get the same record twice because of the current offset.
        //
        // - earliest
        //     означает, что самый(ые) первый(ые) консьюмер текущей
        //     группы, созданный из этих пропертей, получит offset=0,
        //     т. е. будет читать все сообщения с нуля. [GROUP_ID_CONFIG]
        // - latest
        //     the new group doesn't get old replies
        //
        // Also see: https://docs.spring.io/spring-kafka/reference/html/
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); //earliest, none

        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("demo-topic"));

        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<Integer, String> record : records) {
                logger.info("key " + record.key() + " value " + record.value() + " partition " +
                        record.partition() + " offset" + record.offset());
            }

        }

    }
}
