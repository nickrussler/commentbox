(function($) {

		  // Constants
		  var FOLDER = "/commentbox/faces/javax.faces.resource/",
		      EXT = ".gif?ln=editor-icons",
		      BUTTON_COUNT = 12,
		      POPUP_WIDTH = 100;		  	  

		  // Define the icon button
		  $.cleditor.buttons.icon = {
		    name: "icon",
		    css: {
		      background: "URL(" + FOLDER + "1" + EXT + ") 50% 50% no-repeat"
		    },
		    title: "Insert Icon",
		    command: "insertimage",
		    popupName: "Icon",
		    popupHover: true,
		    buttonClick: function(e, data) {
		      $(data.popup).width(POPUP_WIDTH);
		    },
		    popupClick: function(e, data) {
		      data.value = $(e.target).attr('src');
		    }
		  };

		  // Build the popup content
		  var $content = $("<div>");
		  for (var x = 0; x < BUTTON_COUNT; x++) {
		    $('<div><img src="' + FOLDER + (x+1) + EXT + '" />')
		    .css("display", "inline-block")
		    .css('padding', '1px 2px')
		    .appendTo($content);
		  }
		  $.cleditor.buttons.icon.popupContent = $content.children();

		  // Add the button to the default controls
		  $.cleditor.defaultOptions.controls = $.cleditor.defaultOptions.controls
		    .replace("| cut", "icon | cut");

		})(jQuery);