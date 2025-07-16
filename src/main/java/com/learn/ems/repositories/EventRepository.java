package com.learn.ems.repositories;

import com.learn.ems.entity.Event;
import com.learn.ems.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(User organizer);
    List<Event> findByTitleContainingIgnoreCase(String keyword);
}
