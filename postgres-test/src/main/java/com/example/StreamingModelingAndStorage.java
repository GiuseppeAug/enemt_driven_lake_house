package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;

public class StreamingModelingAndStorage {

    private static FileSystem fs;

    static {
        try {
            Configuration hadoopConfig = new Configuration();
            hadoopConfig.set("fs.defaultFS", "hdfs://localhost:9000");
            fs = FileSystem.get(hadoopConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void streamModelingAndStorage() {
        // LOG 4 J
        // BasicConfigurator.configure();
        // Kafka
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streaming-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        String inputTopic = "dos_.public.edge_table";
        String outputTopic = "dos_.public.edge_table-output";

        KStream<String, String> input = builder.stream(inputTopic);

        ObjectMapper objectMapper = new ObjectMapper();

        KStream<String, String> enrichedStream = input.mapValues(value -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(value);
                ObjectNode objectNode = (ObjectNode) jsonNode;
                objectNode.put("greetings", "Hello from Java");
                return objectMapper.writeValueAsString(objectNode);
            } catch (Exception e) {
                e.printStackTrace();
                return value;
            }
        });

        enrichedStream.foreach((key, value) -> {
            try {
                String uniqueFileName = "edge-output-file-" + UUID.randomUUID().toString() + ".txt";
                Path filePath = new Path("/user/hadoop/output-edge-data/" + uniqueFileName);
                try (FSDataOutputStream fsDataOutputStream = fs.create(filePath)) {
                    fsDataOutputStream.writeBytes(value + "\n");
                    fsDataOutputStream.hflush(); // Ensure data is flushed to HDFS immediately
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Send the enriched messages to the output topic
        enrichedStream.to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Build and start the Kafka Streams application
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Add a shutdown hook to gracefully stop the application
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));
    }

}
