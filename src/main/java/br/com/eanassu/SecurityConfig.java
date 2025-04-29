package br.com.eanassu;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private DataSource dataSource;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	private boolean userExists(JdbcUserDetailsManager users, String username) {
		try {
			users.loadUserByUsername(username);
			return true;
		} catch(UsernameNotFoundException e) {
			return false;
		}
	}
    @Bean
    public JdbcUserDetailsManager users(PasswordEncoder encoder) {
    	JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    	String sql1 = "SELECT username,password,enabled from users where username=?";
    	String sql2 = "SELECT username,authority from authorities where username=?";
    	users.setUsersByUsernameQuery(sql1);
    	users.setAuthoritiesByUsernameQuery(sql2);
    	if ( !userExists(users,"admin")) {
    		UserDetails admin=User.builder().username("admin")
    				.password(encoder.encode("admin123"))
    				.roles("ADMIN","USER").build();
    		users.createUser(admin);
    	}
    	if ( !userExists(users,"user")) {
    		UserDetails user=User.builder().username("user")
    				.password(encoder.encode("user123"))
    				.roles("USER").build();
    		users.createUser(user);
    	}
    	return users;
    }


	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth->
		  	auth.requestMatchers("/").permitAll().anyRequest().authenticated())
			.formLogin(login->login.permitAll())
			.logout(logout->logout.permitAll());
		return http.build();
	}

}
