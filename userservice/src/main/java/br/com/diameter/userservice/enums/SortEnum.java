package br.com.diameter.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortEnum {

    ID("Id"),
    NAME("Name"),
    EMAIL("E-mail");

    private final String description;
}
