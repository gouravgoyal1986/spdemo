package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.websocket.Session;

import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Greeting {
	
	@Id
	private long id;
	
	@Column(nullable = false)
	@NotEmpty(message = "Please provide a name")
	private String content;

}

