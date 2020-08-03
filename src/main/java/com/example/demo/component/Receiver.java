package com.example.demo.component;

import org.springframework.stereotype.Component;

import com.example.demo.entity.MoveState;

@Component
public class Receiver {

	  public void receiveMessage(MoveState email) {
	    System.out.println("Received <" + email + ">");
	  }
}
