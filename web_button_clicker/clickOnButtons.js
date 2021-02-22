//###################### Functions ########################\\
/**
*Retrieve all buttons on page and store them in the BUTTONS array
*/
function getAllButtonsOnPage(){
    return document.getElementsByTagName('button');
}
/**
* Keep only the invite buttons in the INVITE_BUTTONS array.
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
    INVITE_BUTTONS.forEach( button => { button.click() } );
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
//#########################################################\\
//######################## Script #########################\\
inviteEverybodyOnPage();
