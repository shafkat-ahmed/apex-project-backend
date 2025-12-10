package com.apex.template.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "sequence")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Sequence {
    @Id
    @GeneratedValue
    private Long id;

    private Long sequenceNo;
}
