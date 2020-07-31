package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.component.DataRequest;
import com.example.demo.component.DataSession;
import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepo;



@Controller
public class HomeController{
	
	@Autowired
	GreetingRepo gr;
	
	@Autowired
	DataRequest dr;
	
	@Autowired
	DataSession ds;
	
	@RequestMapping(value={"/index","/"})
	public String index(Model model) {
		/*System.out.println("SESSION VALUE"+ds.getName());
		System.out.println("SESSION VALUE"+ds.getName().hashCode());
		System.out.println("REQUEST VALUE"+dr.getName());*/
		
		//http://localhost:8080/jenkins/github-webhook/
		model.addAttribute("message", "hello6");
		return "index";
	}
	
	@RequestMapping(value={"/listofperson"})
	public ModelAndView getPerson(Model model,Greeting g) {
		ds.setName("tt");
		dr.setName("tt");
		
		System.out.println("SESSION VALUE"+ds.getName());
		System.out.println("SESSION VALUE"+ds.getName().hashCode());
		System.out.println("REQUEST VALUE"+dr.getName());
		List<Greeting> lis = (List<Greeting>) gr.findAll();
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("listing");
		mv.addObject("listofperson", lis);
		mv.addObject("g", g);
		return mv;
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Object save(@Valid @ModelAttribute Greeting g, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<Greeting> lis = (List<Greeting>) gr.findAll();
			ModelAndView mv = new ModelAndView();
			mv.setViewName("listing");
			mv.addObject("listofperson", lis);
			mv.addObject("g", g);
			
			return mv;
		}
		System.out.println("SESSION VALUE"+ds.getName());
		System.out.println("g.id "+g.getId());
		System.out.println("g.content "+g.getContent());
		gr.save(g);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("greeting");
		
		mv.addObject("greeting",g);
		return "redirect:/listofperson";		
	}
	
	 @ExceptionHandler(value=Exception.class)
	 public String databaseError() {
		 //HttpRequestMethodNotSupported
		 System.out.println("error1>>>>>> ");
	    // Nothing to do.  Returns the logical view name of an error page, passed
	    // to the view-resolver(s) in usual way.
	    // Note that the exception is NOT available to this view (it is not added
	    // to the model) but see "Extending ExceptionHandlerExceptionResolver"
	    // below.
	    return "support";
	  }
	
}