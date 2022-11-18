package com.project.paypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface EntityRepository<T> extends JpaRepository<T,Long> {

    default T get(Long id) {
        Optional<T> entity = findById(id);
        return entity.orElse(null);
    }
}