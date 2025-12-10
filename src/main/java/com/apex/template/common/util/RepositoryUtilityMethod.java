package com.apex.template.common.util;

import com.apex.template.common.exception.EntityNotFoundException;
import com.apex.template.domain.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class RepositoryUtilityMethod {
    /**
     *
     * @param id pass the id of object
     * @param e any repository interface in repository package
     * @return returns an object
     * @param <T>
     * @param <E>
     *
     *     this is a generic method for finding entity
     */
    public static <T extends BaseEntity, E extends CrudRepository<T, Long>> T getObject(Long id, E e){
        Optional<T> optional = e.findById(id);
        if (!optional.isPresent()){
            throw new EntityNotFoundException("no object found with id: " + id);
        }
        return optional.get();
    }
}
