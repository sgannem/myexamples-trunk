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
public class StudentServlet extends HttpServlet {

	private static final long serialVersionUID = 7528373021106530918L;
	
	private Student student;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		// resp.getOutputStream().print(greeting);
		PrintWriter pw = resp.getWriter();
		pw.print("<h3>" + getStudent()
				+ "</h3><hr/> thank you.visit again...");
		
	}
	
	/**
	 * @return the student
	 */
	public Student getStudent() {
		System.out.println(student);
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public @Inject void setStudent(Student student) {
		this.student = student;
	}
	
}
