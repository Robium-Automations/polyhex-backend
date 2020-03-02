package com.robiumautomations.polyhex.configs

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.WebSecurity



@Configuration
@EnableWebSecurity
open class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {

    http
        .authorizeRequests()
        .antMatchers("/login", "/about").permitAll()
  }

  @Throws(Exception::class)
  override fun configure(web: WebSecurity) {
    web.ignoring()
        .antMatchers("/login")
  }

}