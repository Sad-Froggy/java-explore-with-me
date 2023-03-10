package ru.practicum.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.users.model.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByIdIn(Collection<Integer> ids, Pageable page);
}