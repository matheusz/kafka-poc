package com.poc.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.kafka.contract.Event;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.poc.kafka.loader.PropertiesLoader.loadPropertiesFromFile;

public class Producer {

    public static final Logger logger = Logger.getLogger(Producer.class);

    private KafkaProducer<String, String> kafkaProducer;
    private ObjectMapper jsonMapper;

    public Producer() {
        kafkaProducer = new KafkaProducer<>(loadPropertiesFromFile("producer.properties"));
        jsonMapper = new ObjectMapper();
    }

    public void run() {
        IntStream.range(0, 3)
                .forEach(index -> {
                    try {
                        logger.info("Sending event");
                        kafkaProducer.send(produceEvent(index)).get();
                        logger.info("Event sent");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });
    }

    private ProducerRecord<String, String> produceEvent(Integer id) {
        Event event = Event.builder().id(id).values(generateRandomNumbers()).build();
        try {
            return new ProducerRecord<>("random-topic-v1", jsonMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Integer> generateRandomNumbers() {
        return IntStream.range(0, 100)
                .mapToObj(index -> new Random().nextInt(index + 1))
                .collect(Collectors.toList());
    }

}
