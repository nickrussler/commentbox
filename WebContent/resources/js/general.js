function buttonsPressedFix(iter) {
	$.each(iter, function(i, buttonIter){
		if (i < 4) {
			var button = $(buttonIter);
		
			button.unbind('mouseleave');

			button.bind('mouseleave', function(eventObject) {
			  if (!$(this).hasClass('activeEditorButton')) {
				$(this).css('background-color', 'transparent');
			  }
			});

			button.click(function(eventObject) {
			  if ($(this).hasClass('activeEditorButton')) {
				$(this).removeClass('activeEditorButton');
			  } else {
				$(this).addClass('activeEditorButton');
			  }
			});
		}
	});
}