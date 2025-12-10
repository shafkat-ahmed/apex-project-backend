package com.apex.template.repository;


import com.apex.template.domain.Sequence;
import org.springframework.data.repository.CrudRepository;

public interface SequenceRepository extends CrudRepository<Sequence, Long> {
    Sequence getFirstById(Long id);
}
