// Login Form

$(function() {
    var button = $('#loginButton');
    var box = $('#loginBox');
    var form = $('#loginForm');
    var dontclose=false;
    button.removeAttr('href');
    button.mouseup(function(login) {
        box.toggle();
        button.toggleClass('active');
    });
    form.mouseup(function() {
    	dontclose=false;
        return false;
    });
    form.mousedown(function() { 
    	dontclose=true;
    });
  
    $(this).mouseup(function(login) {
        if(!($(login.target).parent('#loginButton').length > 0)) {
           if(!dontclose){
        	   button.removeClass('active');
               box.hide(); 
           }        	
        } 
        dontclose=false;
        
    });
});