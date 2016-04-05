package com.jadyer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class UserController {
	@RequestMapping(value = "/getUserInfo")
	public String getUserInfo(HttpServletRequest request) {
		String currentUser = (String) request.getSession().getAttribute(
				"currentUser");
		System.out.println("��ǰ��¼���û�Ϊ[" + currentUser + "]");
		request.setAttribute("currUser", currentUser);
		return "/user/info";
	}
}
