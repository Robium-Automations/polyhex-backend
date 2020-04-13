package com.robiumautomations.polyhex.security

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : Filter {

  override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    (response as HttpServletResponse).setHeader("Access-Control-Allow-Origin", "*")
//    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
    response.setHeader("Access-Control-Max-Age", "3600")
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With")
    response.setHeader("Access-Control-Expose-Headers", "Content-Type, Authorization, Message")
    chain!!.doFilter(request, response)
  }
}