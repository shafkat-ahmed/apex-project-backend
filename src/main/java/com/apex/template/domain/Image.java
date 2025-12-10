package com.apex.template.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Image extends BaseEntity{

    public Image(String url){
        this.url = url;
    }

    @Column(columnDefinition="LONGTEXT")
    private String url;

    private String documentType;

    private String subDocumentType;

    private String qualificationType;
}
