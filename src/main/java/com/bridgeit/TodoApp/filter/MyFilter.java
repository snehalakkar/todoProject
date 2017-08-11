package com.bridgeit.TodoApp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.bridgeit.TodoApp.DTO.User;
import com.bridgeit.TodoApp.json.Response;
import com.bridgeit.TodoApp.util.TokenManupulation;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyFilter implements Filter {

	TokenManupulation tokenManupulation;
	Response errorResponse;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req1 = (HttpServletRequest) request;
		HttpServletResponse resp1 = (HttpServletResponse) response;

		// getting an access token from header which is set at angular service
		String accToken = req1.getHeader("accessToken");
		/*String trimAccessToken = accToken.substring(1, accToken.length() - 1); // to
																				// remove
																				// outer
																				// quotes
*/
		// objectmapper is use to make an jsonstring response
		ObjectMapper mapper = new ObjectMapper();

		if (accToken != null) {
			System.out.println("inside the dofilter of acc....");
			User user = tokenManupulation.accesstokenValidation(accToken);
			System.out.println("user after checking token valid " + user);
			if (user != null) {
				System.out.println("access is valid....");
				request.setAttribute("userobjInFilter", user);
				chain.doFilter(request, response);
			} else {
				System.out.println("access token is invalid in myfilter....");

				// Object to JSON in String
				errorResponse.setStatus(120);
				errorResponse.setMessage("access token invalid take new access by passing refresh");
				String jsonInString = mapper.writeValueAsString(errorResponse);
				response.getWriter().write(jsonInString);
				return;
			}
		} else {
			System.out.println("Access Token is null");
			errorResponse.setStatus(420);
			errorResponse.setMessage("access token missing");
			String jsonInString = mapper.writeValueAsString(errorResponse);
			response.getWriter().write(jsonInString);
			return;
		}
	}

	public void init(FilterConfig config) throws ServletException {

		// we use WebApplicationContext to create the bean of any service or dao
		// or dto level class inside the filter.
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

		tokenManupulation = context.getBean(TokenManupulation.class);

		errorResponse = context.getBean(Response.class);

	}
}
