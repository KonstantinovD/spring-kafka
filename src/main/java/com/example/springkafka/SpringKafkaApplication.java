package com.example.springkafka;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringKafkaApplication {

  public static final String KAFKA_TOPIC_NAME = "spring-kafka-demo";

  public static void main(String[] args) {


//    consumer.consume();
    MyConsumer consumer = new MyConsumer(KAFKA_TOPIC_NAME);
    consumer.consume();

    try (MyProducer producer = new MyProducer(KAFKA_TOPIC_NAME)) {
      try {
        for (int i=0; i<100; i++) {
          producer.send(String.valueOf(i),
              String.format("hello from [%d] spring", i));
          TimeUnit.SECONDS.sleep(1);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    try {
      TimeUnit.MINUTES.sleep(2);
    } catch (Exception e) {
      e.printStackTrace();
    }

//    consumer.close();
  }
}