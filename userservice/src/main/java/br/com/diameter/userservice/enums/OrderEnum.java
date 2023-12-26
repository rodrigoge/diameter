package br.com.diameter.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum {

    ASC("Asc"),
    DESC("Desc");

    private final String description;
}
