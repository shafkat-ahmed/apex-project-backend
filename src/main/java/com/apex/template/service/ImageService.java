package com.apex.template.service;


import com.apex.template.domain.Image;

public interface ImageService {

    Image addImage(Image image);

    void deleteImage(Image image);

    Image findById(Long id);

    Image findByUrl(String url);

    Image updateImage(Image image);

}
