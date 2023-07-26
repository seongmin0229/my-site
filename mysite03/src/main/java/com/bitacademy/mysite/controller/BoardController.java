package com.bitacademy.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bitacademy.mysite.service.BoardService;
import com.bitacademy.mysite.vo.BoardVo;
import com.bitacademy.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController{
	
	@Autowired
	private BoardService bs;
	
	@RequestMapping({"/list/{no}", "/list", "/"})
	public String list(@PathVariable(name="no", required=false) Integer no, Model model) {
		int selected = 1;
		if(no != null) {
			selected = no;
		}
		int amount = bs.amount();
		int length = amount % 5 == 0 ? amount / 5 : amount / 5 + 1;
		int min = selected % 5 == 0 ? selected - 4 : (selected / 5) * 5 + 1;
		int max = min + 4 > length ? length : min + 4;
		
		model.addAttribute("selected", selected);
		model.addAttribute("length", length);
		model.addAttribute("min", min);
		model.addAttribute("max", max);
		model.addAttribute("list", bs.getBoardList(selected));
		return "board/list";
	}
	
	@RequestMapping(value={"/write", "/write/{no}"}, method=RequestMethod.GET)
	public String write(@PathVariable(name="no", required=false) Long no, Model model) {
		if(no != null) {
			model.addAttribute("no", no);
		}
		return "board/write";
	}
	
	@RequestMapping(value={"/write", "/write/{no}"}, method=RequestMethod.POST)
	public String write(@PathVariable(name="no", required=false) Long no, BoardVo vo, HttpSession session) {
		vo.setUserNo(((UserVo)session.getAttribute("authUser")).getNo());
		bs.write(no, vo);
		System.out.println((UserVo)session.getAttribute("authUser"));
		return "redirect:/board/list";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		model.addAttribute("vo", bs.getBoardByNo(no));
		return "board/view";
	}
	
	@RequestMapping(value="/update/{no}", method=RequestMethod.GET)
	public String update(@PathVariable("no") Long no, Model model) {
		model.addAttribute("vo", bs.getBoardByNo(no));
		return "board/update";
	}
	
	@RequestMapping(value="/update/{no}", method=RequestMethod.POST)
	public String update(@PathVariable("no") Long no, BoardVo vo) {
		vo.setNo(no);
		bs.updateBoard(vo);
		return "redirect:/board/list";
	}
	
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(@PathVariable("no") Long no) {
		bs.delete(no);
		return "redirect:/board/list";
	}
}
