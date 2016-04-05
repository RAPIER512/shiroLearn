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
            System.out.println("��Ϊ�û�[jadyer]������[admin]��ɫ��[admin:manage]Ȩ��");  
            return simpleAuthorInfo;  
		} else if (null != currentUsername && "����".equals(currentUsername)) {
			System.out.println("��ǰ�û�[����]����Ȩ");
			return simpleAuthorInfo;
		}
		return null;  
    }  
   
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {  
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;  
        System.out.println("��֤��ǰSubjectʱ��ȡ��tokenΪ" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));  
//        if("jadyer".equals(token.getUsername())){
        	User user  = userService.getUser("1");
        	System.out.println("��ǰ�û���Ϣ��"+user.toString());
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), this.getName());  
            this.setSession("currentUser", "jadyer");  
            return authcInfo;  
		// }else if("����".equals(token.getUsername())){
		// User user = userService.getUser("2");
		// System.out.println("��ǰ�û���Ϣ��"+user.toString());
		// AuthenticationInfo authcInfo = new
		// SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
		// this.getName());
		// this.setSession("currentUser", "����");
		// return authcInfo;
		// }
		// return null;  
    }  
       
       
    private void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();  
        if(null != currentUser){  
            Session session = currentUser.getSession();  
            System.out.println("SessionĬ�ϳ�ʱʱ��Ϊ[" + session.getTimeout() + "]����");  
            if(null != session){  
                session.setAttribute(key, value);  
            }  
        }  
    }  
}