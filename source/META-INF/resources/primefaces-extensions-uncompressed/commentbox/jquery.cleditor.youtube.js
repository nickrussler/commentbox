/*
 CLEditor YouTube Plugin v1.0.0
 http://www.terapix.de
 requires jQuery v1.4.2 or later, CLEditor WYSIWYG HTML Editor v1.3.0 or later

 Copyright 2012, Markus Horowski, TeraPix.de
 Licensed GPL Version 3 licenses.
*/

(function($)
{
	// Define the vimeo/youtube button
	$.cleditor.buttons.youtube = {
		name: "youtube",
	    css: {
	    	  // EL-will not work here, therefore outer fix was made
		      background: "URL(#{resource['images:film-youtube.png']}) 50% 50% no-repeat" 
		    },
		title: "Insert Video",
		command: "inserthtml",
    popupName: "youtube",
    popupClass: "ui-editor-prompt",
    popupContent:
      "YouTube URL:<br /><input type=text style='width:200px;'>" +
      "<br />or Vimeo URL:<br /><input type=text style='width:200px;'>" +
      "<br />or Dailymotion URL:<br /><input type=text style='width:200px;'><br />" +
      "<input type=button value=Submit>",
    buttonClick: youtubeButtonClick
  };

  // Event handler
  function youtubeButtonClick(e, data) {  
    // Listen to submitting the button
    $(data.popup).children(":button")
      .unbind("click")
      .bind("click", function(e) {
    	  
        // Get the editor
        var editor = data.editor;

        // Get the first text-input value
        var $text = $(data.popup).find(":text"),
          videourl = decodeURI($text[0].value);

        // If first value is set, write youtube embed-code
        if(videourl.length)
        {				
          var regExp = /^(?:https?:\/\/)?(?:www\.)?youtube\.com\/watch\?(?=.*v=((\w|-){11}))(?:\S+)?$/
	      var match = videourl.match(regExp);
          
          if (match&&match[1].length==11)
          {       	  
            var videourl = match[1];
            var html = '<iframe title="YouTube Video Player" class="youtube-player webkitIframeHack" type="text/html" width="570" height="321" src="http://www.youtube.com/embed/' + videourl + '" src2="http://www.youtube.com/embed/' + videourl + '" frameborder="0" wmode="transparant"></iframe><div><br/><br/></div>';
          }
        }

        // Get the second text-input value
        var $text = $(data.popup).find(":text"),
          vimeourl = decodeURI($text[1].value);

        // If second value is set, write vimeo embed-code
        if(vimeourl.length)
        {
          var regExp = /http:\/\/(www\.)?vimeo.com\/(\d+)($|\/)/;
          var match = vimeourl.match(regExp);

          if (match){
            vimeourl = match[2];
            var html = '<iframe class="webkitIframeHack" src="http://player.vimeo.com/video/' + vimeourl + '?color=ffffff" src2="http://player.vimeo.com/video/' + vimeourl + '?color=ffffff" width="570" height="320" frameborder="0" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe><div><br/><br/></div>';
          }
        }
        
        // Get the second text-input value
        var $text = $(data.popup).find(":text"),
          dailymotionurl = decodeURI($text[2].value);

        // If second value is set, write vimeo embed-code
        if(dailymotionurl.length)
        {
          var regExp = /^.+dailymotion.com\/(video|hub)\/([^_]+)[^#]*(#video=([^_&]+))?/;
          var match = dailymotionurl.match(regExp);

          if (match) {
			if(match[4] !== undefined) {
			  dailymotionurl = match[4];
			} else {        	  
			  dailymotionurl = match[2];
			}
			
        	var html = '<iframe frameborder="0" width="480" height="270" class="webkitIframeHack" src="http://www.dailymotion.com/embed/video/' + dailymotionurl + '" src2="http://www.dailymotion.com/embed/video/' + dailymotionurl + '"></iframe><div><br/><br/></div>';
          }
        }

        // Insert the html into the editor
        if (html) {
          editor.execCommand(data.command, html, null, data.button);
        }
        
        $(editor.$frame.contents()[0]).find('body').animate({ scrollTop: $(document).height() }, 'slow');

        // Hide the popup and set the focus
        editor.hidePopups();
        editor.focus();
      });
  }
})(jQuery);