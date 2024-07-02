package com.be.whereu.security.authentication;

import com.be.whereu.model.WhereUJwt;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityManager 객체 작동 테스트")
public class SecurityManagerImplTest {

    @InjectMocks
    private SecurityManagerImpl securityManager ;

    @Mock
    private HttpServletRequest request;

    @Mock
    private WhereUJwt accessToken;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void 보안_컨텍스트_설정_테스트() {
        // Given
        String memberId = "testMemberId";
        when(accessToken.getSubject()).thenReturn(memberId);

        // When
        securityManager.setUpSecurityContext(accessToken, request);

        // Then
        UserDetails userDetails = new User(memberId, "", List.of());
        UsernamePasswordAuthenticationToken expectedAuthToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        expectedAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        verify(securityContext).setAuthentication(refEq(expectedAuthToken));
    }

    @Test
    public void 인증된_사용자_이름_가져오기_테스트() {
        // Given
        String memberId = "testMemberId";
        UserDetails userDetails = new User(memberId, "", List.of());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authToken);

        // When
        String authenticatedUserName = securityManager.getAuthenticatedUserName();

        // Then
        assertThat(authenticatedUserName).isEqualTo(memberId);
    }

    @Test
    public void 인증된_사용자_없을_때_이름_가져오기_테스트() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);

        // When
        String authenticatedUserName = securityManager.getAuthenticatedUserName();

        // Then
        assertThat(authenticatedUserName).isNull();
    }

    @Test
    public void 인증정보가_UserDetails_인스턴스가_아닌_경우_테스트() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // When
        String authenticatedUserName = securityManager.getAuthenticatedUserName();

        // Then
        assertThat(authenticatedUserName).isNull();
    }
}