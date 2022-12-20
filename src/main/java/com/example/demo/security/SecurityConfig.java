package com.example.demo.security;


import com.example.demo.views.LoginView;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;




//@EnableWebSecurity
//@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter{

  /*

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/score").authenticated()
        .and()
        .authorizeRequests()
        .antMatchers("/game").permitAll()
        .and()
        .formLogin();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception{
    super.configure(http);
    setLoginView(http, LoginView.class);
  }



  @Override
  public void configure(WebSecurity web) throws Exception{
    web.ignoring().antMatchers("/images/**");
    super.configure(web);
  }



  @Bean
  @Override
  protected UserDetailsService userDetailsService(){
    return new InMemoryUserDetailsManager(User.withUsername("user")
        .password("{noop}userpass")
        .roles("USER")
        .build());
  }


}

 */
