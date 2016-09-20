package com.poc.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.kafka.contract.Event;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

import static com.poc.kafka.loader.PropertiesLoader.loadPropertiesFromFile;

public class Consumer {

    private static final Logger logger = Logger.getLogger(Consumer.class);

    private KafkaConsumer<String, String> kafkaConsumer;
    private ObjectMapper jsonMapper;

    public Consumer() {
        kafkaConsumer = new KafkaConsumer<>(loadPropertiesFromFile("consumer.properties"));
        jsonMapper = new ObjectMapper();
    }

    public void run() {
        kafkaConsumer.subscribe(Arrays.asList("random-topic-v1"));

        while (true) {
            ConsumerRecords<String, String> events = kafkaConsumer.poll(100);
            logger.info("Got " + events.count() + " events");

            events.forEach(event -> {
                try {
                    Event receivedEvent = jsonMapper.readValue(event.value(), Event.class);
                    logger.info("Received event: " + receivedEvent.toString());
                } catch (IOException error) {
                    logger.error("Error parsing json: " + error.getMessage());
                }
            });
        }
    }

}
