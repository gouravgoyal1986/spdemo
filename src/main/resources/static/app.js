

function getBoard(){
	return $("#board_id").val();
}

function getPlayer(){
	return $("#player_id").val();
}


function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    var boardid = getBoard();
    var playerid= getPlayer();
    
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/board/'+boardid, function (message) {
        	var message = JSON.parse(message.body);
        	if(message.personPlayed != getPlayer()) {
	            //showGreeting(message);
        		setBlueCoin(message.deckno,message.cardno,message.cardtype);
        		clickToggle(false);
	        }
        	if(message.winnerPlayerId!='') {
        		alert("Player "+message.winnerPlayerId+" won the match!!");
        	}
        });
    });
}


function sendCardDetail(deck_no,no,type,position) {
    stompClient.send("/app/hello", {}, JSON.stringify({'deckno': deck_no,'cardno': no,'boardId': getBoard(),'cardtype':type,'personPlayed': getPlayer(),'position':position}));
}



function setCoin(obj) {
	console.log(obj);
	var coins = $('<span class="dotred"> </span>');
	  $(obj).append(coins);
}

function canPlay() {
	if ( $('.deck').attr('disabled') == "disabled" ) { console.log("can't"); return false;}
	return true;
}

function setBlueCoin(deck_no,card_rank,card_name) {
	var coins = $('<span class="dotblue"> </span>');
	  var data = "div[data-deck-no="+deck_no+"][data-card-name="+card_name+"][data-card-rank="+card_rank+"]";
	  console.log("data>>"+data);
	  $(data).append(coins);
}

function clickToggle(val) {
	if(val==true) {
		$(".deck").attr('disabled','disabled');
	} else {
		$(".deck").removeAttr('disabled');
	}	
}

function getPosition(deck_no,no,type,position) {
	console.log('getPosition deck_no '+deck_no+" no "+no+" type "+type," position "+position);
	return parseInt(position);
}

const suitPositions = [
	  [
	    [0, 0]
	  ],
	  [
	    [0, -1],
	    [0, 1, true]
	  ],
	  [
	    [0, -1],
	    [0, 0],
	    [0, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [0, 0],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [-1, 0], [1, 0],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [0, -0.5],
	    [-1, 0], [1, 0],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [0, -0.5],
	    [-1, 0], [1, 0],
	    [0, 0.5, true],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [-1, -1 / 3], [1, -1 / 3],
	    [0, 0],
	    [-1, 1 / 3, true], [1, 1 / 3, true],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [-1, -1], [1, -1],
	    [0, -2 / 3],
	    [-1, -1 / 3], [1, -1 / 3],
	    [-1, 1 / 3, true], [1, 1 / 3, true],
	    [0, 2 / 3, true],
	    [-1, 1, true], [1, 1, true]
	  ],
	  [
	    [0, 0]
	  ],
	  [
	    [0, 0]
	  ]
	];

	const jokerPositions = [
		  [
		    [0, 0]
		  ]
	  ];

	const el = (tagName, attributes, children) => {
	  const element = document.createElement(tagName);

	  if (attributes) {
	    for (const attrName in attributes) {
	      element.setAttribute(attrName, attributes[attrName]);
	    }
	  }

	  if (children) {
	    for (let i = 0; i < children.length; i++) {
	            const child = children[i];

	            if (typeof child === 'string') {
	                element.appendChild(document.createTextNode(child));
	            } else {
	          element.appendChild(child);
	      }
	        }
	  }

	  return element;
	};

	const div = (a, c) => el('div', a, c);

	const decks = '1 2'.split(' ');
	const ranks = '10 8 3 4 9 6 Q 7 2 A 5 K'.split(' ');
	const suits = '♠︎ ♥︎ ♣︎ ♦︎'.split(' ');
	const joker = '🃏'.split(' ');

	const getDeck = (i) => decks[i % 2];
	const getRank = (i) => ranks[i % 12];
	const getSuit = (i) => suits[i / 12 | 0];
	const getJoker = (i) => joker[i / 1 | 0];
	const getColor = (i) => (i / 12 | 0) % 2 ? 'red' : 'black';

	const createSuit = (suit) => (pos) => {
	  const [ x, y, mirrored ] = pos;
	  const mirroredClass = mirrored ? ' mirrored' : '';
	  return div({
	    class: 'card-suit' + mirroredClass,
	    style: `left: ${x * 100}%; top: ${y * 100}%;`
	  }, [ suit ]);
	};

	const createCard = (j,i,counter) => {
	  const deck = getDeck(j);
	  const rank = getRank(i);
	  const suit = getSuit(i);
	  counter = counter+1;
	  const colorClass = 'col-sm card ' + getColor(i);
	  const type = i / 12 | 0;

	  return div({ class: colorClass,'data-deck-no' : deck,'data-card-name' : type,'data-card-rank' : rank,'data-card-position' : counter}, [
	    div({ class: 'card-suits' },
	      suitPositions[i % 12].map(createSuit(suit))
	    ),
	    div({ class: 'card-topleft' }, [
	      div({ class: 'card-corner-rank' }, [
	        rank
	      ]),
	      div({ class: 'card-corner-suit' }, [
	        suit
	      ])
	    ]),
	    div({ class: 'card-bottomright' }, [
	      div({ class: 'card-corner-rank' }, [
	        rank
	      ]),
	      div({ class: 'card-corner-suit' }, [
	        suit
	      ])
	    ])
	  ]);
	};
	
	const createJokerCard = (j,i,counter) => {
		  const deck = getDeck(j);
		  const rank = 'J';
		  const suit = getJoker(i);
		  counter=counter+1;
		  
		  const colorClass = 'col-sm card ' + getColor(i);
		  const type = i / 12 | 0;

		  return div({ class: colorClass,'data-deck-no' : deck,'data-card-name' : counter,'data-card-rank' : rank,'data-card-position' : counter}, [
		    div({ class: 'card-suits' },
		      jokerPositions[i % 1].map(createSuit(suit))
		    ),
		    div({ class: 'card-topleft' }, [
		      div({ class: 'card-corner-rank' }, [
		        rank
		      ]),
		      div({ class: 'card-corner-suit' }, [
		        suit
		      ])
		    ]),
		    div({ class: 'card-bottomright' }, [
		      div({ class: 'card-corner-rank' }, [
		        rank
		      ]),
		      div({ class: 'card-corner-suit' }, [
		        suit
		      ])
		    ])
		  ]);
		};

	const deckCount = new Array(2);
	const cardsData = new Array(48);

	for (let i = 0; i < deckCount.length; i++) {
		deckCount[i] = i;
		}
	
	for (let i = 0; i < cardsData.length; i++) {
	  cardsData[i] = i;
	}

	const deck = div({ class: 'deck container' });
	var rowdiv;
	
	
	let counter =0;
	var card;
	deckCount.forEach((j) => {
		cardsData.forEach((i) => {
			console.log(i+" "+counter);
			if(!(counter==0) && !(counter==9) && !(counter==90) && !(counter==98)) {
				card = createCard(j,i,counter);
				console.log('card created');
			}
			
			i=j+i;
			
			if(counter%10==0) {
				rowdiv = div({ class: 'row' });
			}
			
			if(counter==0 || counter==90) {
				const jokerCard = createJokerCard(0,0,counter);
				rowdiv.appendChild(jokerCard);
			}				
			if(!(counter==0) && !(counter==9) && !(counter==90) && !(counter==98)) {
				console.log('card added');
				rowdiv.appendChild(card);
			}
			
			if(counter==9 || counter==95) {
				const jokerCard = createJokerCard(0,0,counter);
				rowdiv.appendChild(jokerCard);
			}
			if(counter%9==0) {
				deck.appendChild(rowdiv);
			}
			  
		  counter++;
		})
	});
	
	
	
	 
	
	$( document ).ready(function() {
	    console.log( "ready!" );
	    
	    document.body.appendChild(deck);
	    
	    $('.card').click(function(){
	     	 
	     	if(canPlay()) {
	     		 console.log(this.getAttribute('data-card-rank')+this.getAttribute('data-card-name'));
	     		let position = getPosition(this.getAttribute('data-deck-no'),this.getAttribute('data-card-rank'),this.getAttribute('data-card-name'),this.getAttribute('data-card-position'));
		     	 sendCardDetail(this.getAttribute('data-deck-no'),this.getAttribute('data-card-rank'),this.getAttribute('data-card-name'),position);
	     		setCoin(this);
	     		//checkWinner(this.getAttribute('data-deck-no'),this.getAttribute('data-card-rank'),this.getAttribute('data-card-name'));
		     	clickToggle(true);	
	     	} 
	     	
	     });
	});
	

	var stompClient = null;

	function setConnected(connected) {
	    $("#connect").prop("disabled", connected);
	    $("#disconnect").prop("disabled", !connected);
	    if (connected) {
	        $("#conversation").show();
	    }
	    else {
	        $("#conversation").hide();
	    }
	    $("#greetings").html("");
	}

	
	$(function () {
	    $("form").on('submit', function (e) {
	        e.preventDefault();
	    });
	    connect();
	    /*$( "#connect" ).click(function() {  });*/
	    $( "#disconnect" ).click(function() { disconnect(); });
	    $( "#send" ).click(function() { sendName(); });
	});
	
	
	
	
	
	/*****Not in use*****/
	

	function disconnect() {
	    if (stompClient !== null) {
	        stompClient.disconnect();
	    }
	    setConnected(false);
	    console.log("Disconnected");
	}

	function sendName() {
	    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val(),'boardId': getBoard()}));
	}


	function showGreeting(message) {
	    $("#greetings").append("<tr><td>" + message.cardno + "</td><td>" + message.cardtype + "</td></tr>");
	}

	