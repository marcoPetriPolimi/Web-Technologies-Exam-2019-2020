package it.polimi.tiw.pure_html.filter;

import it.polimi.tiw.pure_html.beans.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter
public class UserFilter extends HttpServletFilter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();

		if (session.isNew() || session.getAttribute("user") == null) {
			resp.sendRedirect("/index");
		} else {
			chain.doFilter(request,response);
		}
	}
}
