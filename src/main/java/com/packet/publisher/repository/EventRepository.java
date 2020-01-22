package com.packet.publisher.repository;

import com.packet.publisher.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("EventRepository")
public interface EventRepository extends MongoRepository<Event, String> {
}
