package com.mengyan.xin.web.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zhuojia on 2016/12/7.
 */
public class CrossDominFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*String []  allowDomain= {"http://localhost:63343","http://localhost:8080","http://127.0.0.1:63343","http://127.0.0.1:8080"};
        Set<String> allowedOrigins= new HashSet<String>(Arrays.asList(allowDomain));
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String host= "http://"+httpServletRequest.getHeader("Host");
        String Origin= httpServletRequest.getHeader("Origin");
        System.out.println(host);
        System.out.println(Origin);
        if (allowedOrigins.contains(Origin)){
            ((HttpServletResponse) response).addHeader(
                    "Access-Control-Allow-Origin", Origin
            );
            ((HttpServletResponse) response).addHeader(
                    "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, If-Modified-Since"
            );
            ((HttpServletResponse) response).addHeader(
                    "Access-Control-Allow-Credentials", "true"
            );
            chain.doFilter(request, response);
            return;
        }*/
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Origin", "http://localhost:63343"
        );
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, If-Modified-Since"
        );
        ((HttpServletResponse) response).addHeader(
                "Access-Control-Allow-Credentials", "true"
        );
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
