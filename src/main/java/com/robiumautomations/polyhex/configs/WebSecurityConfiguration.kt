package com.robiumautomations.polyhex.configs

import com.robiumautomations.polyhex.security.JwtAuthenticationFilter
import com.robiumautomations.polyhex.security.JwtAuthorizationFilter
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var userService: UserService

  @Autowired
  private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http.csrf().disable().authorizeRequests()
        .antMatchers(
            "/helloworld", "/health", "/signin", "/universities"
        ).permitAll()
        .antMatchers(HttpMethod.POST, "/users").permitAll()
        .antMatchers(HttpMethod.HEAD, "/usernames/*", "/emails/*").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilter(JwtAuthenticationFilter(authenticationManager()))
        .addFilter(JwtAuthorizationFilter(authenticationManager()))
        // this disables session creation on Spring Security
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
  }

  @Throws(Exception::class)
  public override fun configure(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService<UserDetailsService>(userService).passwordEncoder(bCryptPasswordEncoder)
  }

  @Bean
  open fun corsConfigurationSource(): CorsConfigurationSource {
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
    return source
  }

  @Bean
  open fun allowUrlEncodedSlashHttpFirewall(): HttpFirewall {
    return StrictHttpFirewall().also {
      it.setAllowUrlEncodedSlash(true)
      it.setAllowSemicolon(true)
    }
  }

  @Bean
  open fun corsFilter(): FilterRegistrationBean<*> {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.addAllowedOrigin("*")
    config.addAllowedHeader("*")
    config.addAllowedMethod("*")
    source.registerCorsConfiguration("/**", config)
    val bean: FilterRegistrationBean<*> = FilterRegistrationBean(CorsFilter(source))
    bean.order = 0
    return bean
  }

  override fun configure(web: WebSecurity) {
    super.configure(web)
    web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
  }
}