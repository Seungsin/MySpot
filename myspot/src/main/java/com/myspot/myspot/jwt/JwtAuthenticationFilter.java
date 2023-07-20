package com.myspot.myspot.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myspot.myspot.security.UserDetailsImpl;
import com.myspot.myspot.user.domain.UserRoleEnum;
import com.myspot.myspot.user.domain.repository.UserRepository;
import com.myspot.myspot.user.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            String userEmail = request.getParameter("user_email");
            String password = request.getParameter("password");

            return getAuthenticationManager().authenticate( // authenticate method
                    new UsernamePasswordAuthenticationToken(
                            userEmail,
                            password,
                            null
                    )
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(email, role);
        jwtUtil.addJwtToCookie(token, response);

        System.out.println("authentication : " + email);
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String username = userRepository.findByEmail(email).get().getUsername();
        LoginResponseDto responseDto = new LoginResponseDto(username);

        // json 형태로 바꾸기
        String result = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        System.out.println("exception : " + failed.getMessage());
        response.setStatus(401);
        response.sendError(0, "로그인 실패");
    }
}