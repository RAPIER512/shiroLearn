package com.jadyer.realm;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.jadyer.model.User;
import com.jadyer.service.UserService;

   

public class MyRealm extends AuthorizingRealm {  
    
	@Autowired UserService userService;
	
	
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){  
        String currentUsername = (String)super.getAvailablePrincipal(principals);  
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();  
        if(null!=currentUsername && "jadyer".equals(currentUsername)){  
            simpleAuthorInfo.addRole("admin");  
            simpleAuthorInfo.addStringPermission("admin:manage");  
            System.out.println("已为用户[jadyer]赋予了[admin]角色和[admin:manage]权限");  
            return simpleAuthorInfo;  
		} else if (null != currentUsername && "玄玉".equals(currentUsername)) {
			System.out.println("当前用户[玄玉]无授权");
			return simpleAuthorInfo;
		}
		return null;  
    }  
   
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {  
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;  
        System.out.println("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));  
//        if("jadyer".equals(token.getUsername())){
        	User user  = userService.getUser("1");
        	System.out.println("当前用户信息："+user.toString());
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), this.getName());  
            this.setSession("currentUser", "jadyer");  
            return authcInfo;  
		// }else if("玄玉".equals(token.getUsername())){
		// User user = userService.getUser("2");
		// System.out.println("当前用户信息："+user.toString());
		// AuthenticationInfo authcInfo = new
		// SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
		// this.getName());
		// this.setSession("currentUser", "玄玉");
		// return authcInfo;
		// }
		// return null;  
    }  
       
       
    private void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();  
        if(null != currentUser){  
            Session session = currentUser.getSession();  
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");  
            if(null != session){  
                session.setAttribute(key, value);  
            }  
        }  
    }  
}