<%@ page language="java" pageEncoding="UTF-8"%>  
   
<script type="text/javascript">  
<!--  
function reloadVerifyCode(){  
    document.getElementById('verifyCodeImage').setAttribute('src', '${pageContext.request.contextPath}/mydemo/getVerifyCodeImage');  
}  
//-->  
</script>  
   
<div style="color:red; font-size:22px;">${message_login}</div>  
   
<form action="<%=request.getContextPath()%>/login" method="POST">  
    姓名：<input type="text" name="username"/><br/>  
    密码：<input type="text" name="password"/><br/>  
    验证：<input type="text" name="verifyCode"/>  
         &nbsp;&nbsp;  
         <img id="verifyCodeImage" onclick="reloadVerifyCode()" src="<%=request.getContextPath()%>/getVerifyCodeImage"/><br/>  
    <input type="submit" value="确认"/>  
</form>