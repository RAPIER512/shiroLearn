package com.jadyer.controller;

import java.awt.Color;  
import java.awt.image.BufferedImage;  
import java.io.IOException;  
   
import javax.imageio.ImageIO;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
   
import org.apache.commons.lang3.StringUtils;  
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;  
import org.apache.commons.lang3.builder.ToStringStyle;  
import org.apache.shiro.SecurityUtils;  
import org.apache.shiro.authc.AuthenticationException;  
import org.apache.shiro.authc.ExcessiveAttemptsException;  
import org.apache.shiro.authc.IncorrectCredentialsException;  
import org.apache.shiro.authc.LockedAccountException;  
import org.apache.shiro.authc.UnknownAccountException;  
import org.apache.shiro.authc.UsernamePasswordToken;  
import org.apache.shiro.subject.Subject;  
import org.apache.shiro.web.util.WebUtils;  
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.servlet.view.InternalResourceViewResolver;  
   
import com.jadyer.util.VerifyCodeUtil;  
@Controller  
@RequestMapping("mydemo")  
public class LoginController {  
    @RequestMapping("/getVerifyCodeImage")  
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        response.setHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);  
        request.getSession().setAttribute("verifyCode", verifyCode);  
        System.out.println("�������ɵ���֤��Ϊ[" + verifyCode + "],�Ѵ�ŵ�HttpSession��");  
        response.setContentType("image/jpeg");  
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);  
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());  
    }  
       
       
    @RequestMapping(value="/login", method=RequestMethod.POST)  
    public String login(HttpServletRequest request){  
        String resultPageURL = InternalResourceViewResolver.FORWARD_URL_PREFIX + "/";  
        String username = request.getParameter("username");  
        String password = request.getParameter("password");  
        String verifyCode = (String)request.getSession().getAttribute("verifyCode");  
        String submitCode = WebUtils.getCleanParam(request, "verifyCode");  
        System.out.println("�û�[" + username + "]��¼ʱ�������֤��Ϊ[" + submitCode + "],HttpSession�е���֤��Ϊ[" + verifyCode + "]");  
        if (StringUtils.isEmpty(submitCode) || !StringUtils.equals(verifyCode, submitCode.toLowerCase())){  
            request.setAttribute("message_login", "��֤�벻��ȷ");  
            return resultPageURL;  
        }  
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);  
        token.setRememberMe(true);  
        System.out.println("Ϊ����֤��¼�û�����װ��tokenΪ" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));  
        Subject currentUser = SecurityUtils.getSubject();  
        try {  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤��ʼ");  
            currentUser.login(token);  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤ͨ��");  
            resultPageURL = "main";  
        }catch(UnknownAccountException uae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,δ֪�˻�");  
            request.setAttribute("message_login", "δ֪�˻�");  
        }catch(IncorrectCredentialsException ice){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�����ƾ֤");  
            request.setAttribute("message_login", "���벻��ȷ");  
        }catch(LockedAccountException lae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�˻�������");  
            request.setAttribute("message_login", "�˻�������");  
        }catch(ExcessiveAttemptsException eae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,�����������");  
            request.setAttribute("message_login", "�û�������������������");  
        }catch(AuthenticationException ae){  
            System.out.println("���û�[" + username + "]���е�¼��֤..��֤δͨ��,��ջ�켣����");  
            ae.printStackTrace();  
            request.setAttribute("message_login", "�û��������벻��ȷ");  
        }  
        if(currentUser.isAuthenticated()){  
            System.out.println("�û�[" + username + "]��¼��֤ͨ��(������Խ���һЩ��֤ͨ�����һЩϵͳ������ʼ������)");  
        }else{  
            token.clear();  
        }  
        return resultPageURL;  
    }  
       
       
    @RequestMapping("/logout")  
    public String logout(HttpServletRequest request){  
         SecurityUtils.getSubject().logout();  
         return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";  
    }  
}