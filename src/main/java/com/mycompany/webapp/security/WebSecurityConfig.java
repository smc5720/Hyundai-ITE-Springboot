package com.mycompany.webapp.security;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	private DataSource dataSource;

	@Resource
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("HttpSecurity http 실행");

		// 로그인 방식 설정
		http.formLogin().loginPage("/security/loginForm") // default: /login(GET)
				.usernameParameter("mid") // default: username
				.passwordParameter("mpassword") // default: password
				.loginProcessingUrl("/login") // default: /login(POST)
				.defaultSuccessUrl("/security/content").failureUrl("/security/loginError"); // default: /login?error
		// 로그아웃 설정
		http.logout().logoutSuccessUrl("/logout") // default: /logout
				.logoutSuccessUrl("/security/content");
		// URL 권한 설정
		http.authorizeRequests().antMatchers("/security/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/security/manager/**").hasAuthority("ROLE_MANAGER").antMatchers("/security/user/**")
				.authenticated().antMatchers("/**").permitAll();
		// 권한 없음(403)일 경우 이동할 경로 설정
		http.exceptionHandling().accessDeniedPage("/security/accessDenied");
		// CSRF 비활성화
		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("AuthenticationManagerBuilder auth 실행");
		/*auth.jdbcAuthentication().dataSource(dataSource)
				// 데이터베이스에서 가져올 사용자 정보 조회 설정
				.usersByUsernameQuery("SELECT mid, mpassword, menabled FROM member WHERE mid=?")
				.authoritiesByUsernameQuery("SELECT mid, mrole FROM member WHERE mid=?")
				// 패스워드 인코딩 방법 설정
				.passwordEncoder(passwordEncoder()); // default: DelegatingPasswordEncoder
		*/
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		log.info("WebSecurity web 실행");
		// 권한 계층 설정
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchyImpl());
		web.expressionHandler(handler);
		web.ignoring().antMatchers("/bootstrap-4.6.0-dist/**").antMatchers("/css/**").antMatchers("/images/**")
				.antMatchers("/jquery/**").antMatchers("/favicon.ico");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		// PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}

	// 권한 계층을 참조하기 위해 HttpSecurity에서 사용하기 때문에 관리빈으로 반드시 등록해야 함
	@Bean
	public RoleHierarchyImpl roleHierarchyImpl() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
		return roleHierarchyImpl;
	}
}