package com.apex.template.service;

import com.apex.template.domain.BaseEntity;

public interface DtoGenericCrudService<T>{
    T create(T t) throws Exception;
    T update(T t) throws Exception;
    T readDto(Long id) throws Exception;
}
