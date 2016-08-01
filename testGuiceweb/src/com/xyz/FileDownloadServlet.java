package com.xyz;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FileDownloadServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IUtil util;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		resp.setContentType("text/html; charset=UTF-8");
//		resp.setCharacterEncoding("UTF-8");
		String mimeType = "application/octet-stream";
		resp.setContentType(mimeType);
//        resp.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "appstore.apk");
//		String headerValue = String.format("attachment; filename=\"%s\"", "test.txt");
        resp.setHeader(headerKey, headerValue);
//	    resp.getOutputStream().write(util.readFile("appstore.apk").getBytes("UTF-8"));
	    ServletOutputStream outStream = resp.getOutputStream();
	    byte[] buffer = new byte[4096];
        int bytesRead = -1;
        InputStream inStream = getClass().getResourceAsStream("appstore.apk");
//        InputStream inStream = getClass().getResourceAsStream("test.txt");
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inStream.close();
        outStream.close();     
	}
	
	@Inject public void setUtil(IUtil util) {
		this.util = util;
	}
	
}
