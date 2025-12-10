package com.apex.template.service.implementation;


import com.apex.template.domain.Image;
import com.apex.template.repository.ImageRepository;
import com.apex.template.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    //private final ImageRepository imageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image addImage(Image image) {

        Image i =findByUrl(image.getUrl());
        if(i==null) {
            return imageRepository.save(image);
        }
        return i;
    }


    public void deleteImage(Image image) {

        imageRepository.delete(image);

    }

    @Override
    public Image findById(Long id) {

        return imageRepository.getFirstById(id);
    }

    @Override
    public Image findByUrl(String url) {

        return imageRepository.findByUrl(url);
    }

    @Override
    public Image updateImage(Image image) {
        // TODO Auto-generated method stub
        return imageRepository.save(image);
    }
}
