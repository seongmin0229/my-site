package com.bitacademy.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bitacademy.mysite.service.UserService;
import com.bitacademy.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService us;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join() {
		return "user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(UserVo vo) {
		us.addUser(vo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping("/joinsuccess")
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(HttpSession session, UserVo vo, Model model) {
		UserVo authUser = us.getUser(vo);
		
//		UserVo authUser = null;
		if(authUser == null) {
			model.addAttribute("result", "fail");
			return "/user/login";
		}
		
		/* 로그인 처리 */
		session.setAttribute("authUser", authUser);
		
		return "redirect:/";
	}
	
	@RequestMapping("logout")
	public String login(HttpSession session) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		// 접근제어
		if(authUser == null) {
			return "redirect:/";
		}
		////////////////////////////////
		
		/* 로그아웃 처리 */
		session.removeAttribute("authUser");
		session.invalidate();
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(HttpSession session, Model model) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		// 접근제어
		if(authUser == null) {
			return "redirect:/";
		}
		////////////////////////////////
		Long no = authUser.getNo();
		UserVo userVo = us.getUser(no);
		model.addAttribute("userVo", userVo);
		
		return "/user/update";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(HttpSession session, UserVo vo) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		// 접근제어
		if(authUser == null) {
			return "redirect:/";
		}
		////////////////////////////////
		
		vo.setNo(authUser.getNo());
		us.updateUser(vo);
		authUser.setName(vo.getName());
		authUser.setGender(vo.getGender());
		return "redirect:/user/update";
	}
	
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(Exception e) {
//		
//		return "error/exception";
//	}
}