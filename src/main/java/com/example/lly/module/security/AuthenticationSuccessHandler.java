package com.example.lly.module.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component("authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.setCharacterEncoding("utf-8");

        if(principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            request.getSession().setAttribute("userDetails", user);
            String token = JwtTokenUtil.createToken(user);

            response.setHeader("token", JwtTokenUtil.TOKEN_PREFIX + token);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"ifSuccess\": \"true\", \"content\": \"登录成功\"}");
            out.flush();
            out.close();
        }
        logger.info("登录成功！");
    }

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

}
