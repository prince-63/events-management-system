package com.learn.ems.repositories;

import com.learn.ems.entity.Event;
import com.learn.ems.entity.Registration;
import com.learn.ems.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByUserAndEvent(User user, Event event);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findAllByEventId(Long eventId);

    List<Registration> findAllByUserId(Long userId);

}
