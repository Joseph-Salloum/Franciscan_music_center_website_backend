package music_center_backend.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import music_center_backend.security.jwt.JwtAuthenticationEntryPoint;
import music_center_backend.security.jwt.JwtAuthenticationFilter;
import music_center_backend.security.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService, authenticationEntryPoint);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // @Test
    // @DisplayName("shouldNotFilter should skip public auth endpoints")
    // void shouldNotFilterShouldSkipPublicEndpoints() throws ServletException {
    //     MockHttpServletRequest request = new MockHttpServletRequest("GET", "/auth/login");

    //     assertEquals(true, filter.shouldNotFilter(request));
    // }

    // @Test
    // @DisplayName("shouldNotFilter should not skip protected endpoints")
    // void shouldNotFilterShouldNotSkipProtectedEndpoints() throws ServletException {
    //     MockHttpServletRequest request = new MockHttpServletRequest("GET", "/students/1");

    //     assertEquals(false, filter.shouldNotFilter(request));
    // }

    @Test
    @DisplayName("doFilterInternal should continue when Authorization header is missing")
    void doFilterInternalShouldContinueWithoutAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/students/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        verify(jwtService, never()).validateAndExtractPublicId(any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("doFilterInternal should authenticate a valid bearer token")
    void doFilterInternalShouldAuthenticateValidBearerToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/students/1");
        request.addHeader("Authorization", "Bearer token-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        UserDetails userDetails = new User("user-123", "password", authorities);

        when(jwtService.validateAndExtractPublicId("token-123")).thenReturn("user-123");
        when(userDetailsService.loadUserByUsername("user-123")).thenReturn(userDetails);

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("user-123", authentication.getName());
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals(authorities, authentication.getAuthorities());
    }

    @Test
    @DisplayName("doFilterInternal should not replace an existing authentication")
    void doFilterInternalShouldNotReplaceExistingAuthentication() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/students/1");
        request.addHeader("Authorization", "Bearer token-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "existing-user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))));

        when(jwtService.validateAndExtractPublicId("token-123")).thenReturn("user-123");

        filter.doFilter(request, response, filterChain);

        assertEquals("existing-user", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(userDetailsService, never()).loadUserByUsername(eq("user-123"));
    }

    @Test
    @DisplayName("doFilterInternal should invoke entry point when JWT is invalid")
    void doFilterInternalShouldInvokeEntryPointOnInvalidJwt() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/students/1");
        request.addHeader("Authorization", "Bearer broken-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtService.validateAndExtractPublicId("broken-token"))
                .thenThrow(new BadCredentialsException("Invalid JWT token"));

        filter.doFilter(request, response, filterChain);

        verify(authenticationEntryPoint).commence(eq(request), eq(response), any(BadCredentialsException.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}