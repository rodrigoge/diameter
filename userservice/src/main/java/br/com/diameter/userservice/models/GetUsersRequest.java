package br.com.diameter.userservice.models;

import br.com.diameter.userservice.enums.OrderEnum;
import br.com.diameter.userservice.enums.SortEnum;

public record GetUsersRequest(String name, String email, int offset, int limit, SortEnum sortEnum, OrderEnum orderEnum) {
}
