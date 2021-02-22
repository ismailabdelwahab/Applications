//###################### Functions ########################\\
/**
*Retrieve all buttons on page and store them in the BUTTONS array
*/
function getAllButtonsOnPage(){
	return document.getElementsByTagName('button');
}
/**
* Extract from BUTTONS only the invite buttons.
*/
function extractInviteButtons( BUTTONS ){
	var INVITE_BUTTONS = [];
	for(let i=0; i<BUTTONS.length; i++){
	  	var ariaLabel = BUTTONS[i].getAttribute('aria-label')
		if(  ariaLabel != null && ariaLabel.startsWith("In") )
			INVITE_BUTTONS.push( BUTTONS[i] );
	}
	return INVITE_BUTTONS;
}

/**
* Click on all buttons inside INVITE_BUTTONS array.
*/
function clickOnAllButtons( INVITE_BUTTONS ){
	INVITE_BUTTONS.forEach( 
		button => { button.click() } 
	);
}

/**
* Clicks on all the buttons on this page that invites someone.
*/
function inviteEverybodyOnPage(){
	var BUTTONS = getAllButtonsOnPage();
	var INVITE_BUTTONS = extractInviteButtons( BUTTONS );
	clickOnAllButtons( INVITE_BUTTONS );
	console.log("You just added " + INVITE_BUTTONS.length + " personnes.")
}

//################### Scroll down #########################\\
function scrollDownForXmillis( ms ){
	times = [];
	for( let i=0; i < ms; i+=1000 ){
		times.push(i);
	}
	for( let i=0; i < times.length; i++ ){
		setTimeout( function(){window.scrollBy(0,5000);}, times[i] )
	}
}

//######################## Script #########################\\
let scrollingTime=10_000;
scrollDownForXmillis(scrollingTime);
setTimeout( inviteEverybodyOnPage(), scrollingTime+1000 );
