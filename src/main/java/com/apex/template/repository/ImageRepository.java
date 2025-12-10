package com.apex.template.repository;


import com.apex.template.domain.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Image findById (long id);

    Image findByUrl(String url);

    Image getFirstById(long id);
}
