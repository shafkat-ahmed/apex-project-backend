package com.apex.template.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    @NotBlank
    private String name;

    @Size(min = 2, max = 100, message = "Username character must be between 2 to 100!")
    private String username;

    @Size(min = 6)
    private String password;

    private String phone;

    private Long roleId;
}