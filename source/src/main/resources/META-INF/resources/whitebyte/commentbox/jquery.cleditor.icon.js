/*
 CLEditor Icon Plugin v1.0
 http://premiumsoftware.net/cleditor
 requires CLEditor v1.2 or later

 Copyright 2010, Chris Landowski, Premium Software, LLC
 Dual licensed under the MIT or GPL Version 2 licenses.
 
 Adjusted for commentBox by Nick Russler
 */
function initJQueryCLEditorIconPlugin(baseIconURL, smileyCount, smileyPopupWidth) {
	// Constants
	var baseSplit = baseIconURL.split('/1.gif?ln=')
		FOLDER = baseSplit[0] + '/',
		EXT = ".gif?ln=" + baseSplit[1],
		BUTTON_COUNT = smileyCount,
		POPUP_WIDTH = smileyPopupWidth;

	// Define the icon button
	$.cleditor.buttons.icon = {
		name : "icon",
		css : {
			background : "URL(" + FOLDER + "1" + EXT + ") 50% 50% no-repeat"
		},
		title : "Insert Icon",
		command : "insertimage",
		popupName : "Icon",
		popupHover : true,
		buttonClick : function(e, data) {
			$(data.popup).width(POPUP_WIDTH);
		},
		popupClick : function(e, data) {
			data.value = $(e.target).attr('src');
		}
	};

	// Build the popup content
	var $content = $("<div>");
	for ( var x = 0; x < BUTTON_COUNT; x++) {
		$('<div><img src="' + FOLDER + (x + 1) + EXT + '" />').css("display", "inline-block").css('padding', '1px 2px').appendTo($content);
	}
	$.cleditor.buttons.icon.popupContent = $content.children();
}