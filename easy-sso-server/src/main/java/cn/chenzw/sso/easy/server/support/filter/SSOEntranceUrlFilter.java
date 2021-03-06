package cn.chenzw.sso.easy.server.support.filter;

import cn.chenzw.sso.easy.core.constants.SSOConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenzw
 */
@Component
public class SSOEntranceUrlFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (StringUtils.endsWith(request.getRequestURI(), SSOConstants.DEFAULT_SSO_HANDLE_MAPPING)
                || !StringUtils.endsWith(request.getRequestURI(), SSOConstants.ENTRANCE_URI)) {
            filterChain.doFilter(request, response);
        }

        request.getRequestDispatcher(SSOConstants.DEFAULT_SSO_HANDLE_MAPPING).forward(request, response);
    }
}
