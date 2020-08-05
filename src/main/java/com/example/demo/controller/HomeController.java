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
import com.example.demo.entity.Player;
import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepo;

@Controller
public class HomeController{
	
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    public final int JACK_INITIAL = 1;
    public final int JACK_UPPER_RIGHT = 10;
    public final int JACK_LOWER_LEFT = 91;
    public final int JACK_LAST = 100;
    
    HashMap<Long,Player> myMap = new HashMap<>();
    
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
		
		model.addAttribute("message", "hello10");
		return "index";
	}
	
	
	
	@RequestMapping(value={"/cequence/{boardId}/{playerId}"})
	public String cequenceIndex(Model model,@PathVariable("boardId") long boardId,@PathVariable("playerId") long playerId) {
		model.addAttribute("boardId",boardId);
		if(!myMap.containsKey(playerId)) {
			Player _player = new Player();
			_player.setPlayerId(playerId);
			_player.setMyMoveList(new ArrayList<>());
			myMap.put(playerId, _player);
		}				
		model.addAttribute("playerId",playerId);
		return "../static/index";
	}
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	@MessageMapping("/hello")
	  public void greeting(MoveState message) throws Exception {
	    System.out.println("message>>>"+message);
	    boolean isWinner = false;

	    System.out.println("message getPersonPlayed>>>"+message.getPersonPlayed());
	    System.out.println("message myMap get player>>>"+  myMap.get((long)message.getPersonPlayed()));
	    
	    myMap.forEach((i,v)->{ System.out.println(i); 
	    System.out.println(v);
	    });
	    
		isWinner = isWinner(myMap.get((long)message.getPersonPlayed()).getMyMoveList(),message.getPosition());
    	myMap.get((long)message.getPersonPlayed()).getMyMoveList().add(message.getPosition());
	    
	    if(isWinner) {
	    	myMap.get((long)message.getPersonPlayed()).getMyMoveList().clear();
	    	message.setWinnerPlayerId(message.getPersonPlayed());	
	    }
	    
	    
	    this.template.convertAndSend("/topic/board/"+message.getBoardId(), message);
	  }
	
	
	private boolean isWinner(ArrayList<Integer> myList, int move) {
		System.out.println("isWinner checking move "+move);
		System.out.println("isWinner checking myList ");
		myList.forEach(i->System.out.println(i));
		
		
		if(myList.isEmpty()) return false;
		int verticalFound = 1;
		int horizontalFound = 1;
		int verticalDiagonalFound = 1;
		int horizontalDiagonalFound = 1;
		Boolean verticalDownCrawlingStopped = false;
		Boolean verticalUpCrawlingStopped = false;
		
		if(move==JACK_INITIAL || move==JACK_UPPER_RIGHT  || move==JACK_LOWER_LEFT  || move==JACK_LAST) {
			verticalFound++;
			horizontalFound++;
			verticalDiagonalFound++;
			horizontalDiagonalFound++;
		}
		for(int i=1;i<5;i++) {
			if(!verticalUpCrawlingStopped && myList.contains(move - (i*10))) {
				if(move - (i*10)==JACK_INITIAL
						|| move - (i*10)==JACK_UPPER_RIGHT
						|| move - (i*10)==JACK_LOWER_LEFT  
						|| move - (i*10)==JACK_LAST) {
					verticalFound++;
				}
				verticalFound++;
				System.out.println("isWinner verticalUpCrawling "+(move - (i*10)));
			} else {
				verticalUpCrawlingStopped = true;
			}
			if(!verticalDownCrawlingStopped && myList.contains(move + (i*10))) {
				if(move + (i*10)==JACK_INITIAL
						|| move + (i*10)==JACK_UPPER_RIGHT
						|| move + (i*10)==JACK_LOWER_LEFT
						|| move + (i*10)==JACK_LAST) {
					verticalFound++;
				}
				verticalFound++;
				System.out.println("isWinner verticalDownCrawling "+(move + (i*10)));
			} else {
				verticalDownCrawlingStopped = true;
			}
		}
		System.out.println("isWinner verticalFound "+verticalFound);
		
		Boolean horizontalLeftCrawlingStopped = false;
		Boolean horizontalRightCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			
			if(!horizontalLeftCrawlingStopped && (move - i)%10!=0 && myList.contains(move - i)) {
				if((move - i)==JACK_INITIAL
						|| (move - i)==JACK_UPPER_RIGHT
						|| (move - i)==JACK_LOWER_LEFT
						|| (move - i)==JACK_LAST ) {
					horizontalFound++;
				}
				horizontalFound++;
				System.out.println("isWinner horizontalLeftCrawling "+(move - i));
			}else {
				horizontalLeftCrawlingStopped = true;
			}
			if(!horizontalRightCrawlingStopped && (move + i)%10!=1 && myList.contains(move + i)) {
				if((move + i)==JACK_INITIAL
						|| (move + i)==JACK_UPPER_RIGHT
						|| (move + i)==JACK_LOWER_LEFT
						|| (move + i)==JACK_LAST) {
					horizontalFound++;
				}
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
		
		Boolean verticalDiagonalDownCrawlingStopped = false;
		Boolean verticalDiagonalUpCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			if(!verticalDiagonalUpCrawlingStopped && myList.contains(move - (i*11))) {
				if(move - (i*11)==JACK_INITIAL || move - (i*11)==JACK_UPPER_RIGHT || move - (i*11)==JACK_LOWER_LEFT || move - (i*11)==JACK_LAST) {
					verticalDiagonalFound++;
				}
				verticalDiagonalFound++;
				System.out.println("isWinner verticalUpCrawling "+(move - (i*11)));
			} else {
				verticalDiagonalUpCrawlingStopped = true;
			}
			if(!verticalDiagonalDownCrawlingStopped && myList.contains(move + (i*11))) {
				if(move + (i*11)==JACK_INITIAL
						|| move + (i*11)==JACK_UPPER_RIGHT
						|| move + (i*11)==JACK_LOWER_LEFT
						|| move + (i*11)==JACK_LAST) {
					verticalDiagonalFound++;
				}
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
		
		Boolean horizontalDiagonalLeftCrawlingStopped = false;
		Boolean horizontalDiagonalRightCrawlingStopped = false;
		for(int i=1;i<5;i++) {
			
			if(!horizontalDiagonalLeftCrawlingStopped && (move - (i*9))%10!=1 && myList.contains(move - (i*9))) {
				if(move - (i*9)==JACK_INITIAL || move - (i*9)==JACK_UPPER_RIGHT || move - (i*9)==JACK_LOWER_LEFT || move - (i*9)==JACK_LAST) {
					horizontalDiagonalFound++;
				}
				horizontalDiagonalFound++;
				System.out.println("isWinner horizontalDiagonalLeftCrawling "+(move + (i*9)));
			}else {
				horizontalDiagonalLeftCrawlingStopped = true;
			}
			if(!horizontalDiagonalRightCrawlingStopped && (move + (i*9))%10!=0 && myList.contains(move + (i*9))) {
				if(move + (i*9)==JACK_INITIAL || move + (i*9)==JACK_UPPER_RIGHT || move + (i*9)==JACK_LOWER_LEFT || move + (i*9)==JACK_LAST) {
					horizontalDiagonalFound++;
				}
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