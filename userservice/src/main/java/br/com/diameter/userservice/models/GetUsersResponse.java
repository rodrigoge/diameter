package br.com.diameter.userservice.models;

import java.util.List;

public record GetUsersResponse(List<UserResponse> users, int totalNumberOfRecords) {
}
