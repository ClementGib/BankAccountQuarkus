package com.cdx.bas.application.mapper;

public interface DtoEntityMapper <M, E> {
    
    /**
     * Maps entity object E to model M
     * 
     * @param entity object
     * @return model object
     */
    M toDto(E entity);

    /**
     * Maps model object M to entity E
     * 
     * @param model object
     * @return entity object
     */
    E toEntity(M model);

}
