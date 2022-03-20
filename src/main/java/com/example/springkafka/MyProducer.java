package com.example.springkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class MyProducer implements Closeable {

  private final String topic;
  // producer - Closeable
  private KafkaProducer<String, String> kafkaProducer;


  public MyProducer(String topic) {
    this.topic = topic;
    Properties props = new Properties();
    props.setProperty(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty( // различаем коннекшены к кафке
        ProducerConfig.CLIENT_ID_CONFIG, "clientId");
    // сериализатор объектов (key/value objects)
    props.setProperty(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    props.setProperty(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    kafkaProducer = new KafkaProducer<>(props);
  }

  public RecordMetadata send(@NonNull String key, String value)
      throws ExecutionException, InterruptedException {
    ProducerRecord<String, String> record =
        new ProducerRecord<>(topic, key, value);
    return kafkaProducer.send(record) // async, returns future
        .get();
  }

  @Override
  public void close() {
    kafkaProducer.close();
  }
}
