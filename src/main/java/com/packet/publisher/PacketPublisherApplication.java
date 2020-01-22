package com.packet.publisher;

import com.packet.publisher.repository.EventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = EventRepository.class)
public class PacketPublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacketPublisherApplication.class, args);
    }

}
