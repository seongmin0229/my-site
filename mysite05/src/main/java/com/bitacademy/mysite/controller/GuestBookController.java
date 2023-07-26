package com.bitacademy.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bitacademy.mysite.security.Auth;
import com.bitacademy.mysite.service.GuestBookService;
import com.bitacademy.mysite.vo.GuestBookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestBookController {
	
	@Autowired
	private GuestBookService gbs;
	
	@Auth
	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("list", gbs.getMessageList());
		return "guestbook/list";
	}
	
	@RequestMapping("/add")
	public String add(GuestBookVo vo) {
		gbs.addMessage(vo);
		return "redirect:/guestbook/list";
	}
	
	@RequestMapping("delete/{no}")
	public String delete(@PathVariable("no") Long no, Model model) {
		model.addAttribute("no", no);
		return "guestbook/delete";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(GuestBookVo vo) {
		gbs.deleteMessage(vo);
		return "redirect:/guestbook/list";
	}
}
