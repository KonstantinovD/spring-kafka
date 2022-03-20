package com.example.springkafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyConsumer implements Closeable {

  private KafkaConsumer<String, String> kafkaConsumer;

  public MyConsumer(String topic) {
    Properties props = new Properties();
    props.setProperty(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty( // идентификатор консьюмера
        ConsumerConfig.GROUP_ID_CONFIG, "groupId");
    // сериализатор объектов (key/value objects)
    props.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    props.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());

    kafkaConsumer = new KafkaConsumer<>(props);
    kafkaConsumer.subscribe(Collections.singletonList(topic));
  }

  public void consume() {
    new Thread(() -> {
      while (true) {
        ConsumerRecords<String, String> records
            = kafkaConsumer.poll(Duration.ofSeconds(1));
        records.forEach(record -> {
//          recordsConsumer.ac
          System.out.printf("offset = %d, key = %s, value = %s%n",
              record.offset(), record.key(), record.value());
        });
      }
    }).start();
  }

  @Override
  public void close() {
    kafkaConsumer.close();
  }
}
