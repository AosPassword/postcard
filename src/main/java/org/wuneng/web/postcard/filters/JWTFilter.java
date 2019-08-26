package org.wuneng.web.postcard.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.Constant;
import org.wuneng.web.postcard.utils.JWTUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter
public class JWTFilter implements Filter {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Constant constant;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;


        String tokenStr = httpServletRequest.getHeader("token");
        if (tokenStr == null || tokenStr.equals("")) {
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.print("token is null");
            printWriter.flush();
            printWriter.close();
            return;
        }

        // 验证JWT的签名，返回CheckResult对象
        CheckResult checkResult = JWTUtil.validateJWT(tokenStr);
        if (checkResult.isSuccess()) {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        } else {
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.print(checkResult.getErrCode());
            printWriter.flush();
            printWriter.close();
        }
    }

    @Override
    public void destroy() {

    }
}
