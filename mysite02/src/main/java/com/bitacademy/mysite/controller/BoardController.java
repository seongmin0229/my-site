package com.bitacademy.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bitacademy.mysite.dao.BoardDao;
import com.bitacademy.mysite.vo.BoardVo;
import com.bitacademy.mysite.vo.UserVo;

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String actionName = request.getParameter("a");
		if("write".equals(actionName)) {
			HttpSession session = request.getSession();
			if(session.getAttribute("authUser") == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			BoardVo parentVo = null;
			if(request.getParameter("no") != null) {
				parentVo = new BoardDao().findByNo(Long.parseLong(request.getParameter("no")));
			}
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardVo newVo = new BoardVo();
			newVo.setTitle(title);
			newVo.setContent(content);
			newVo.setUserNo(((UserVo)session.getAttribute("authUser")).getNo());
			new BoardDao().insert(parentVo, newVo);
			
			response.sendRedirect(request.getContextPath() + "/board");
		}else if("writeform".equals(actionName)) {
			request
			.getRequestDispatcher("/WEB-INF/views/board/writeform.jsp")
			.forward(request, response);
		}else if("view".equals(actionName)) {
			Long no = Long.parseLong(request.getParameter("no"));
			BoardVo vo = new BoardDao().findByNo(no);
			request.setAttribute("vo", vo);
			request
			.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
		}else if("delete".equals(actionName)) {
			
		}else if("deleteform".equals(actionName)) {
			
		}else {
			/* default action */
			int selected = 1;
			if(request.getParameter("p") != null) {

				selected = Integer.parseInt(request.getParameter("p"));
			}
			int amount = new BoardDao().length();
			int length = amount % 5 == 0 ? amount / 5 : amount / 5 + 1;
			int min = selected % 5 == 0 ? selected - 4 : (selected / 5) * 5 + 1;
			int max = min + 4 > length ? length : min + 4;
			
			request.setAttribute("selected", selected);
			request.setAttribute("length", length);
			request.setAttribute("min", min);
			request.setAttribute("max", max);
			request.setAttribute("list", new BoardDao().findAllByRange(selected));
			request
				.getRequestDispatcher("/WEB-INF/views/board/list.jsp")
				.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
