package br.com.diameter.userservice.db;

import br.com.diameter.userservice.builders.MockBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    private User user;

    @Test
    void shouldCreateUserObject() {
        var user = MockBuilder.createUser();
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isNotNull();
        Assertions.assertThat(user.isAccountNonExpired()).isTrue();
        Assertions.assertThat(user.isCredentialsNonExpired()).isTrue();
        Assertions.assertThat(user.isAccountNonLocked()).isTrue();
        Assertions.assertThat(user.isEnabled()).isTrue();
    }
}
