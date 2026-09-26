package com.uv.inventario.web1;

import com.uv.inventario.bean.SesionBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SesionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String pagina = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        SesionBean sesion = (SesionBean) httpRequest.getSession().getAttribute("sesionBean");

        if (pagina.endsWith("/login.xhtml") || (sesion != null && sesion.isAutenticado())) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/login.xhtml");
        }
    }
}