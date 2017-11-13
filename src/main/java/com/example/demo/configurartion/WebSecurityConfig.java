package com.example.demo.configurartion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import com.example.demo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	

        RequestHeaderAuthenticationFilter siteMinderFilter = new RequestHeaderAuthenticationFilter();
        siteMinderFilter.setAuthenticationManager(authenticationManager());
        siteMinderFilter.setPrincipalRequestHeader("CORPID");
        siteMinderFilter.setExceptionIfHeaderMissing(false);
        
        http.addFilter(siteMinderFilter)
                .authorizeRequests()
                
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                 .logout()
                .permitAll().and().csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	
      auth.authenticationProvider( preAuthProvider());
       
       //auth.inMemoryAuthentication()
        //      .withUser("user").password("password").roles("USER");
      //auth.ldapAuthentication().u
      auth
		.ldapAuthentication()
			.userDnPatterns("uid={0},ou=people")
			.groupSearchBase("ou=groups")
			.contextSource()
				.url("ldap://localhost:8389/dc=springframework,dc=org")
				.and()
			.passwordCompare()
				.passwordEncoder(new LdapShaPasswordEncoder())
				.passwordAttribute("userPassword");
    }
    @Bean
	public PreAuthenticatedAuthenticationProvider preAuthProvider() {
		PreAuthenticatedAuthenticationProvider paaProvider = new PreAuthenticatedAuthenticationProvider();
    	 UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                 userDetailsService());
    	 	paaProvider.setPreAuthenticatedUserDetailsService(wrapper);
		return paaProvider;
	}
    
    @Bean
    public UserDetailsService userDetailsService() {
    		return new CustomUserDetailsService();
    }
}