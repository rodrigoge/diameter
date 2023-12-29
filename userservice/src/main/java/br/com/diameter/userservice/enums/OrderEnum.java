package br.com.diameter.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum {

    ASC("ASC"),
    DESC("DESC");

    private final String description;
}
