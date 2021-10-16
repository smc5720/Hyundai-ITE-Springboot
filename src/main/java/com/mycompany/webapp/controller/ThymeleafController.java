package com.mycompany.webapp.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.webapp.dto.Board;

import lombok.extern.java.Log;

@Controller
@Log
@RequestMapping("/thymeleaf")
public class ThymeleafController {

	@RequestMapping("/content")
	public String content() {
		log.info("실행");
		return "/thymeleaf/content";
	}

	@RequestMapping("/text")
	public String text(Model model) {
		log.info("실행");

		Board board = new Board();

		board.setBno(1);
		board.setBtitle("Spring Boot Template Engine");
		board.setBcontent(
				"<span style='color:red'>Thymeleaf</span> is a modern server-side <b>Java template engine</b>");
		board.setMid("thymeleaf");
		board.setBdate(new Date());

		model.addAttribute("board", board);

		return "thymeleaf/text";
	}

	@RequestMapping("/javascript")
	public String javascript(Model model) {
		log.info("실행");

		model.addAttribute("name", "홍길동");
		model.addAttribute("hobby", new String[] { "영화", "여행", "드라이빙" });

		Board board = new Board();
		board.setBno(1);
		board.setBtitle("스프링 부트 Template Engine");
		board.setBcontent("Thymeleaf is a modern server-side Java template engine");
		board.setMid("thymeleaf");
		board.setBdate(new Date());
		model.addAttribute("board", board);

		JSONObject jsonObject = new JSONObject(board);
		model.addAttribute("jsonBoard", jsonObject.toString());

		return "thymeleaf/javascript";
	}

	@RequestMapping("/variableExpressions")
	public String variableExpressions(HttpSession session) {
		log.info("실행");

		if (session.getAttribute("sessionMid") == null) {
			session.setAttribute("sessionMid", "thymeleaf");
		} else {
			session.removeAttribute("sessionMid");
		}

		return "thymeleaf/variableExpressions";
	}

	@RequestMapping("/selectionVariableExpressions")
	public String selectionVariableExpressions(Model model) {
		log.info("실행");
		Board board = new Board();
		board.setBno(1);
		board.setBtitle("Spring Boot Template Engine");
		board.setBcontent(
				"<span style='color:red'>Thymeleaf</span> is a modern server-side <b>Java template engine</b>");
		board.setMid("thymeleaf");
		board.setBdate(new Date());
		model.addAttribute("board", board);

		return "thymeleaf/selectionVariableExpressions";
	}
	
	@RequestMapping("/messageExpressions")
	public String messageExpressions(Model model) {
		log.info("실행");

		return "thymeleaf/messageExpressions";
	}
}
