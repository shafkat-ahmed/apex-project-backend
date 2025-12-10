package com.apex.template.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PageableResponseDto<T> {
    private long totalNumberOfElements=0;
    private List<T> data;
}
