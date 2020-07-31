package com.example.demo.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandling {

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		System.out.println("ExceptionHandling:: HttpRequestMethodNotSupportedException>>>>>> ");

	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", ex.getMessage());
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("support");
	    
	    return mav;
	  }
	
	@ExceptionHandler(value=Exception.class)
	 public String databaseError() {
		 //HttpRequestMethodNotSupported
		 System.out.println("ExceptionHandling:: Exception>>>>>> ");
	    // Nothing to do.  Returns the logical view name of an error page, passed
	    // to the view-resolver(s) in usual way.
	    // Note that the exception is NOT available to this view (it is not added
	    // to the model) but see "Extending ExceptionHandlerExceptionResolver"
	    // below.
	    return "support";
	  }
	
}
