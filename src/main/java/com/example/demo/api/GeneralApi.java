package com.example.demo.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepo;

@RestController
public class GeneralApi {
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@Autowired
	private GreetingRepo greetingRepo;
	
	@GetMapping(value="/api/test")
	public ResponseEntity<List<List<String>>> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		
		System.out.println("inside allperson");
		List<List<String>> lis = new ArrayList<>(); 
		lis.add(Arrays.asList("as","asda"));
		lis.add(Arrays.asList("as","asda"));
		for(List<String> l: lis) {
			System.out.println(l);
		}
		
        return ResponseEntity.ok(lis);
		
		//return new ResponseEntity<List<List<String>>>(lis,HttpStatus.OK);
	}
	
	@PostMapping("/api/greeting")
	Greeting newGreeting(@Valid @RequestBody Greeting rb) {
		System.out.println(rb.getId());
		//System.out.println(rb.getContent().toString());
		return greetingRepo.save(rb);		
	}
	
	@GetMapping("/api/greeting")
	public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		List<Greeting> ls = new ArrayList<>();
				
		ls.add(new Greeting(counter.incrementAndGet(), String.format(template, name+counter.incrementAndGet())));
		ls.add(new Greeting(counter.incrementAndGet(), String.format(template, name)));
		return ls;
	}
	
	@RequestMapping(value="/api/hello")
	public List<String> getHello() {
		List<String> ls = new ArrayList<>();
		Greeting g = new Greeting(counter.incrementAndGet(), String.format(template, "hi"));
		ls.add(g.getContent());
		ls.add(g.getContent());
		return ls;
	}
}
