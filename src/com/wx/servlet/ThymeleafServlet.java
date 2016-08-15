package com.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet(name = "/thymeleaf",urlPatterns = "*.html")
public class ThymeleafServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContextTemplateResolver resolver = null;
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		resolver = new ServletContextTemplateResolver(config.getServletContext());
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setPrefix("/");
		resolver.setCacheable(false);
		resolver.setCharacterEncoding("utf-8");
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doService(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doService(request, response);
	}

	private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		
		WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
		String templateName = getTemplateName(request);
		String result = engine.process(templateName, context);
		PrintWriter out = null;
		try
		{
			out = response.getWriter();
			out.println(result);
		}
		finally
		{
			out.close();
		}
	}


	private String getTemplateName(HttpServletRequest request)
	{
		String requestPath = request.getRequestURI();
		String contextPath = request.getContextPath();
		if(contextPath == null)
		{
			contextPath = "";
		}
		return requestPath.substring(contextPath.length());
	}

}
