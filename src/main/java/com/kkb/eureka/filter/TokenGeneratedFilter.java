package com.kkb.eureka.filter;


import com.kkb.eureka.token.TokenGenerated;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author alexzhang
 * @date 2020/6/24 15:31
 */
public class TokenGeneratedFilter implements Filter {

    TokenGenerated generated;
    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        String uid=request.getParameter("uid");
        String amount=request.getParameter("amount");
        if(generated.getTokenByLocalCache(uid, Long.parseLong(amount))){
            chain.doFilter(request, servletResponse);
        }else{
            HttpServletResponse response= (HttpServletResponse) servletResponse;
            String ret = new String("产品已卖光");
            response.getOutputStream().write(ret.getBytes());
            response.flushBuffer();
            return;
        }
    }
}
