package com.mengyan.xin.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * Created by Zhuojia on 16/5/23.
 */
public class ORLogicalRolesFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        Subject subject = this.getSubject(request, response);
        String[] rolesArray = ((String[])mappedValue);
        if(rolesArray != null && rolesArray.length != 0) {
            List<String> roles = CollectionUtils.asList(rolesArray);
            boolean[] hasRoles = subject.hasRoles(roles);
            for (boolean hasRole:hasRoles) {
                if (hasRole) {
                    return true;
                }
            }
        }
        return false;
    }
}
