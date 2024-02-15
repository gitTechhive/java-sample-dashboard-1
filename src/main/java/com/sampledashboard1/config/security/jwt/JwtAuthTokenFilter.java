package com.sampledashboard1.config.security.jwt;

import com.sampledashboard1.config.security.CustomUserDetailsService;
import com.sampledashboard1.repository.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UsersRepository usersRepository;

    private final JwtProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwt(request);
        String userId = null;
        String tokenInvalid = "TOKEN_INVALID";
        if(userId == null) {


            try {
                userId = tokenProvider.extractUsername(jwt);
            } catch (IllegalArgumentException e) {
                log.warn("Unable to get JWT Token");
                request.setAttribute(tokenInvalid, "Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token has expired");
                request.setAttribute(tokenInvalid, "Session expired");
            } catch (SignatureException ex) {
                log.warn("Invalid JWT Signature");
                request.setAttribute(tokenInvalid, "Invalid JWT Signature");
            } catch (MalformedJwtException ex) {
                log.warn("Invalid JWT token");
                request.setAttribute(tokenInvalid, "Invalid JWT token");
            } catch (UnsupportedJwtException ex) {
                log.warn("Unsupported JWT exception");
                request.setAttribute(tokenInvalid, "Unsupported JWT exception");
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            UserDetails userDetails = userDetailsService.loadUserById(userId,response);

            if (Boolean.TRUE.equals(tokenProvider.validateToken(jwt, userDetails))) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

//            // on every request new token generation logic
//            String newJwt = tokenProvider.generateJwtToken(authentication);
//            response.addHeader(AUTHORIZATION, newJwt);
//            response.setHeader("Access-Control-Expose-Headers", AUTHORIZATION);
            }
        }

        filterChain.doFilter(request, response);

    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.replace(TOKEN_PREFIX, "");
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }
        return null;
    }

}
