package com.bitacademy.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitacademy.mysite.dao.GuestBookDao;
import com.bitacademy.mysite.vo.GuestBookVo;

public class GuestBookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String actionName = request.getParameter("a");
		if("insert".equals(actionName)) {
			System.out.println("insert");
			GuestBookVo vo = new GuestBookVo();
			vo.setName(request.getParameter("name"));
			vo.setPassword(request.getParameter("pass"));
			vo.setText(request.getParameter("content"));
			new GuestBookDao().insert(vo);
			response.sendRedirect(request.getContextPath() + "/guestbook");
		}else if("deleteform".equals(actionName)) {
			request
				.getRequestDispatcher("WEB-INF/views/guestbook/deleteform.jsp")
				.forward(request, response);
		}else if("delete".equals(actionName)) {
			GuestBookVo vo = new GuestBookVo();
			vo.setNo(Long.parseLong(request.getParameter("no")));
			vo.setPassword(request.getParameter("password"));
			new GuestBookDao().delete(vo);
			
			response.sendRedirect(request.getContextPath() + "/guestbook");
		}else{
			request.setAttribute("list", new GuestBookDao().findAll());
			request
				.getRequestDispatcher("WEB-INF/views/guestbook/list.jsp")
				.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
