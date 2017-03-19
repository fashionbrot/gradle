package com.dongnao.jack.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AccessInterceptor implements HandlerInterceptor {

	private static ObjectMapper objectMapper = new ObjectMapper( );
	static {
		objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
	}

	/*@Autowired
	protected AccessService accessService;*/

	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
		boolean validate = false;
		String accessToken = request.getParameter( "access_token" );
		/*Accesstoken accesstoken = accessService.validateAccessToken( accessToken );
		if ( accesstoken != null ) {
			Calendar cal1 = Calendar.getInstance( );
			Calendar cal2 = Calendar.getInstance( );
			cal1.setTime( new Date( ) );
			cal2.setTime( accesstoken.getCreattime( ) );
			cal2.add( Calendar.SECOND, accesstoken.getExpiresIn( ) );
			if ( cal2.after( cal1 ) ) {
				validate = true;
			}
			request.setAttribute( Constants.ACCESS_TOKEN_KEY, accesstoken );
		}*/

		if ( !validate && accessToken != null && accessToken.equals( "1122334455" ) ) {
			validate = true;
		}

		if ( !validate )
			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
		return validate;

	}

	@Override
	public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView )
			throws Exception {

	}

	@Override
	public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
		if ( ex instanceof ServletException ) {
			if ( ex.getMessage( ).indexOf( "Could not resolve view with name" ) >= 0 ) {
				response.sendError( HttpServletResponse.SC_NOT_FOUND );
			}
		}
	}

}
