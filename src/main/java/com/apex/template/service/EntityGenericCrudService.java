package com.apex.template.service;


import com.apex.template.domain.BaseEntity;

public interface EntityGenericCrudService<T extends BaseEntity>{
    T create(T t) throws Exception;
    T update(T t) throws Exception;
    T read(Long id) throws Exception;
    void delete(Long id) throws Exception;
}
