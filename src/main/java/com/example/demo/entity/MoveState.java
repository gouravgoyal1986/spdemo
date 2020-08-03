package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveState {
	int deckno;
	int boardId;
	String cardno;
	int cardtype;
	int personPlayed;
	int position;
	int winnerPlayerId;
}
