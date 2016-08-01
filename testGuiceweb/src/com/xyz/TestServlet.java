package com.xyz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 7528373021106530918L;

	private String greeting;

	@Inject
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		// resp.getOutputStream().print(greeting);
		PrintWriter pw = resp.getWriter();
		pw.print("<h3>" + greeting
				+ "</h3> <hr/> Please <a href=\"download\">download Appstore Android application</a><br/>"
				+ "<a href=\"student\">new student</a>"
				+ "<hr/> thank you.visit again...");
		
	}
}
