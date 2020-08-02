package com.example.demo.component;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.MoveState;

@Component
public class Receiver {

	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	  public void receiveMessage(MoveState email) {
	    System.out.println("Received <" + email + ">");
	  }
}
