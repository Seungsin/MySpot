//package com.myspot.myspot.config;
//
//import com.myspot.myspot.user.service.UserSecurityService;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////@RequiredArgsConstructor
//@Configuration //스프링의 환경설정 파일
//@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만듦.
//@AllArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private UserSecurityService userSecurityService;
//
//    @Bean //AuthenticationManager 은 스프링 시큐리티의 인증을 담당
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception
//    {
//        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
//        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                // 페이지 권한 설정, 접근 제한
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/myinfo").hasRole("MEMBER")
//                .antMatchers("/**").permitAll()
//
//                .and() // 로그인 설정
//                .formLogin()//form기반으로 인증을 하도록 함, 로그인 정보는 기본적으로 HttpSession을 이용함
//                .loginProcessingUrl("/user/login")
//                .usernameParameter("userEmail")
//                .passwordParameter("password")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        System.out.println("authentication : " + authentication.getName());
//                        response.setStatus(200);
//                        response.sendRedirect("/user/login/"+authentication.getName());
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                        System.out.println("exception : " + exception.getMessage());
//                        response.setStatus(401);
//                        response.sendError(0, "로그인 실패");
//                    }
//                })
//                .permitAll()
//
//                .and()// 로그아웃 설정
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/user/logout/result")
//                .invalidateHttpSession(true)
//                .and()
//
//                // 403 예외처리 핸들링
//                .exceptionHandling().accessDeniedPage("/user/denied")
//                .and()
//                .csrf().disable();
//                //.ignoringAntMatchers("/user/signup")
//                //.ignoringAntMatchers("/user/test");
//    }
//}