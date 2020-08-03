package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.example.demo.component.DataRequest;
import com.example.demo.component.DataSession;
import com.example.demo.entity.MoveState;
import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepo;



@Controller
public class HomeController{
	
    Logger logger = LoggerFactory.getLogger(HomeController.class);
	
    ArrayList<Integer> myList1 = new ArrayList<Integer>();//For two player only
    ArrayList<Integer> myList2 = new ArrayList<Integer>();//For two player only
    
	@Autowired
	GreetingRepo gr;
	
	@Autowired
	DataRequest dr;
	
	@Autowired
	DataSession ds;
	
	@RequestMapping(value={"/index","/"})
	public String index(Model model) {
		System.out.println("SESSION VALUE"+ds.getName());
		System.out.println("SESSION VALUE"+ds.getName().hashCode());
		System.out.println("REQUEST VALUE"+dr.getName());
		
		//http://localhost:8080/jenkins/github-webhook/
		model.addAttribute("message", "hello10");
		return "index";
	}
	
	
	
	@RequestMapping(value={"/cequence/{boardId}/{playerId}"})
	public String cequenceIndex(Model model,@PathVariable("boardId") long boardId,@PathVariable("playerId") long playerId) {
		model.addAttribute("boardId",boardId);
		model.addAttribute("playerId",playerId);
		return "../static/index";
	}
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	@MessageMapping("/hello")
	  public void greeting(MoveState message) throws Exception {
	    Thread.sleep(1000); // simulated delay
	    System.out.println("message>>>"+message);
	    boolean isWinner = false;
		if(message.getPersonPlayed() == 1) {
			isWinner = isWinner(myList1,message.getPosition());
	    	myList1.add(message.getPosition());	
	    } else {
	    	isWinner = isWinner(myList2,message.getPosition());
	    	myList2.add(message.getPosition());
	    	
	    }	    
	    
	    if(isWinner) {
	    	myList1.clear();
	    	myList2.clear();
	    	message.setWinnerPlayerId(message.getPersonPlayed());	
	    }
	    
	    
	    this.template.convertAndSend("/topic/board/"+message.getBoardId(), message);
	    
	    //return new Greeting(1,"Hello, " + message.getPersonPlayed() + "!");
	  }
	
	
	private boolean isWinner(ArrayList<Integer> myList, int move) {
		System.out.println("isWinner checking");
		if(myList.isEmpty()) return false;
		int verticalFound = 1;
		Boolean verticalDownCrawlingStopped = false;
		Boolean verticalUpCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			if(!verticalUpCrawlingStopped && myList.contains(move - (i*10))) {
				verticalFound++;
				System.out.println("isWinner verticalUpCrawling "+(move - (i*10)));
			} else {
				verticalUpCrawlingStopped = true;
			}
			if(!verticalDownCrawlingStopped && myList.contains(move + (i*10))) {
				verticalFound++;
				System.out.println("isWinner verticalDownCrawling "+(move + (i*10)));
			} else {
				verticalDownCrawlingStopped = true;
			}
		}
		System.out.println("isWinner verticalFound "+verticalFound);
		int horizontalFound = 1;
		Boolean horizontalLeftCrawlingStopped = false;
		Boolean horizontalRightCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			
			if(!horizontalLeftCrawlingStopped && (move - i)%10!=0 && myList.contains(move - i)) {
				horizontalFound++;
				System.out.println("isWinner horizontalLeftCrawling "+(move - i));
			}else {
				horizontalLeftCrawlingStopped = true;
			}
			if(!horizontalRightCrawlingStopped && (move + i)%10!=1 && myList.contains(move + i)) {
				horizontalFound++;
				System.out.println("isWinner horizontalRightCrawling "+(move + i));
			}else {
				horizontalRightCrawlingStopped = true;
			}
		}
		System.out.println("isWinner horizontalFound "+horizontalFound);
		
		/*1
		   1
		    1*/
		int verticalDiagonalFound = 1;
		Boolean verticalDiagonalDownCrawlingStopped = false;
		Boolean verticalDiagonalUpCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			if(!verticalDiagonalUpCrawlingStopped && myList.contains(move - (i*11))) {
				verticalDiagonalFound++;
				System.out.println("isWinner verticalUpCrawling "+(move - (i*11)));
			} else {
				verticalDiagonalUpCrawlingStopped = true;
			}
			if(!verticalDiagonalDownCrawlingStopped && myList.contains(move + (i*11))) {
				verticalDiagonalFound++;
				System.out.println("isWinner verticalDiagonalDownCrawling "+(move + (i*11)));
			} else {
				verticalDiagonalDownCrawlingStopped = true;
			}
		}
		System.out.println("isWinner verticalDiagonalFound "+verticalDiagonalFound);
		
		/* 1
		  1
		 1 */
		int horizontalDiagonalFound = 1;
		Boolean horizontalDiagonalLeftCrawlingStopped = false;
		Boolean horizontalDiagonalRightCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			
			if(!horizontalDiagonalLeftCrawlingStopped && (move - (i*9))%10!=1 && myList.contains(move - (i*9))) {
				horizontalDiagonalFound++;
				System.out.println("isWinner horizontalDiagonalLeftCrawling "+(move + (i*9)));
			}else {
				horizontalDiagonalLeftCrawlingStopped = true;
			}
			if(!horizontalDiagonalRightCrawlingStopped && (move + (i*9))%10!=0 && myList.contains(move + (i*9))) {
				horizontalDiagonalFound++;
				System.out.println("isWinner horizontalDiagonalRightCrawling "+(move + (i*9)));
			}else {
				horizontalDiagonalRightCrawlingStopped = true;
			}
		}
		System.out.println("isWinner horizontalDiagonalFound "+horizontalDiagonalFound);
		
		if(verticalFound>4 || horizontalFound>4||verticalDiagonalFound>4 || horizontalDiagonalFound>4) {
			return true;
		}
		
		myList.forEach(
				(i)->{ 
					System.out.println(i);
					
				}				
				);
		
		return false;
	}



	@RequestMapping(value={"/listofperson"})
	public ModelAndView getPerson(Model model,Greeting g) {
		ds.setName("tt");
		dr.setName("tt");
		
		logger.debug("<<<<<<<<<<<<testing>>>>>>>>>>>>>>>");
		
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