package com.apex.template.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OfficeDto {
    private Long id;
    private String name;
    private String officeCode;

    private Long officeTypeId;
    private OfficeTypeDto officeType;

    private Long districtId;
    private DistrictDto district;

    private String reportingOfficeType;

    @JsonProperty
    private Boolean isHillTractOffice;
    private String upazilaType;
}
