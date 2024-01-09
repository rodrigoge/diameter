package br.com.diameter.userservice.filters;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal_ValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        String mockToken = "mocked-token";
        String mockEmail = "john.doe@example.com";
        var mockUser = new User();
        mockUser.setEmail(mockEmail);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        Mockito.when(tokenService.validateToken(mockToken)).thenReturn(mockEmail);
        Mockito.when(userRepository.findByEmail(mockEmail)).thenReturn(Optional.of(mockUser));
        authenticationFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(userRepository).findByEmail(ArgumentMatchers.eq(mockEmail));
        Mockito.verify(tokenService).validateToken(ArgumentMatchers.eq(mockToken));
        Mockito.verify(filterChain).doFilter(ArgumentMatchers.any(HttpServletRequest.class), ArgumentMatchers.any(HttpServletResponse.class));
        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(Optional.of(mockUser), authentication.getPrincipal());
        Assertions.assertNull(authentication.getCredentials());
        Assertions.assertEquals(mockUser.getAuthorities(), authentication.getAuthorities());
    }
}
