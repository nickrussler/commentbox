function commentboxWidget(ccid, userid) {
	
	var _self = this;
	
	this.comment_id = '.comments-' + ccid;

	this.isAnswerEditorVisible = false;
	this.isEditEditorVisible = false;

	this.resizefunc_edit = function(){};
	this.resizefunc_answer = function(){};

	this.oldNodes = new Array();

	this.isUserTyping_timestamp = 0;

	this.newAnswers = {};

	this.rcUserTyping_callback = function(){};

	this.init = function () {		
		$(window).resize(function() {
			_self.repositionEditors();
		});				

		$(function(){
			_self.timer();							
			_self.initUserTypingEvents();

			if ($(_self.comment_id + ' .comment_editor').first().find('textarea').attr('disabled') != 'disabled') {
				editorClickHandler = function () {
					_self.cancelEdit();
					_self.cancelCreateAnswer();
					
					var $editorForm = $(_self.comment_id + ' .comment_editor').first();
					var $editor = $editorForm.find('.ui-editor');
					$($editorForm.find('iframe').contents()[0]).find('body').html('');
					$editorForm.removeClass('comment_editor-height-hack');
					$(this).unbind('click', editorClickHandler);
					$(_self.comment_id + ' .comment_editorButtons').fadeIn();
					$editor.find('.ui-editor-toolbar').fadeIn();
				};
				
				$($(_self.comment_id + ' .comment_editor').first().find('iframe').contents()[0]).bind('click', editorClickHandler);
			}
		});
	};
	
	this.init();

	this.repositionEditors = function () {
		if (_self.isAnswerEditorVisible) {
			_self.resizefunc_answer();
		}	
		
		if (_self.isEditEditorVisible) {
			_self.resizefunc_edit();
		}
	};

	this.reShowEditors = function () {
		if (_self.isAnswerEditorVisible) {
			_self.showAnswerEditor();
		}

		if (_self.isEditEditorVisible) {
			_self.showInlineEditor(true);
		}
	};

	this.initDiffNodes = function () {			
		_self.oldNodes = new Array();				
    	$(_self.comment_id + ' .ui-treenode:visible').each(function(){
    		var str = $(this).attr('class');
    		var startPos = str.indexOf('treenode-unique-id-');
    		var endPos = str.indexOf(' ', startPos);
    		var uniqueID = str.substring(startPos, endPos);
        	
    		_self.oldNodes.push(uniqueID);
        });
	};

	this.indicateNewNodes = function () {
		var newNodes = new Array();
		
    	$(_self.comment_id + ' .ui-treenode:visible').each(function(){
    		var str = $(this).attr('class');
    		var startPos = str.indexOf('treenode-unique-id-');
    		var endPos = str.indexOf(' ', startPos);
    		var uniqueID = str.substring(startPos, endPos);
        	
    		newNodes.push(uniqueID);
        });

        var diffNodes = $.grep(newNodes, function(n, i) {
        	  return $.inArray(n, _self.oldNodes) == -1;
        });

        for (var i = 0; i < diffNodes.length; i++) {              	
            $(_self.comment_id + ' .' + diffNodes[i].replace(/:/g, '\\:')).find('.newIndicator').show().fadeOut(10000, function() {
            	_self.repositionEditors();
            });
        }
	};

	this.scrollTo = function (b){
		var $elem = $(PrimeFaces.escapeClientId(b));
		var a = $elem.offset();
		$("html,body").animate({scrollTop:(a.top - ($(window).height() / 2)) - $elem.height(), scrollLeft:a.left},{easing:"easeInCirc"}, 1000)
	};

	this.isScrolledIntoView = function (elem){
		var docViewTop = $(window).scrollTop();
		var docViewBottom = docViewTop + $(window).height();
		var elemTop = $(elem).offset().top;
		var elemBottom = elemTop + 50; // $(elem).height();
		return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
	};

	this.scrollToIfNotInView = function (elem) {
		if (!_self.isScrolledIntoView(elem)) {
			_self.scrollTo($(elem).attr('id'));
		}
	};

	this.saveCurrentEditComment = function (CommentID) {
		$(_self.comment_id + ' .inlineEditorForm #currentEditComment').attr('value', CommentID);
	};
	
	this.showInlineEditor = function (isReshow) {
		var comment = $(_self.comment_id + ' .inlineEditorForm #currentEditComment').attr('value');
			
		_self.isEditEditorVisible = true;	
		$(_self.comment_id + ' .comment-text').show();				
		$(_self.comment_id + ' .commentEditorDiv').hide();				
		$(_self.comment_id + ' .comment-' + comment + ' .body .comment-text').hide();
		$(_self.comment_id + ' .comment-' + comment + ' .body .commentEditorDiv').show();
		_self.resizefunc_edit = function() {
			$(_self.comment_id + ' .inlineEditorForm').attr('style', '').css($(_self.comment_id + ' .comment-' + comment + ' .body .commentEditorDiv').first().offset()).css('position', 'absolute');
		};		
		_self.repositionEditors();
		
		if (!isReshow) {		
			$(_self.comment_id + ' .inlineEditorForm .inlineEditor iframe').contents().find('body').html($(_self.comment_id + ' .comment-' + comment + ' .body .comment-text').html());
		}
		
		_self.scrollToIfNotInView($(_self.comment_id + ' .commentEditorDiv'));
	};

	this.submitEdit = function () {
		var edit_comment_id = $(_self.comment_id + ' #currentEditComment').attr('value');
		
		$(_self.comment_id + ' .comment-' + edit_comment_id +' .body .comment-text').html($(_self.comment_id + ' .inlineEditorForm .inlineEditor iframe').contents().find('body').html());
		_self.cancelEdit();
	};

	this.cancelEdit = function () {
		_self.isEditEditorVisible = false;
		var edit_comment_id = $(_self.comment_id + ' #currentEditComment').attr('value');
		
		$(_self.comment_id + ' .inlineEditorForm').attr('style', 'height: 0px; overflow: hidden');
		$(_self.comment_id + ' .comment-' + edit_comment_id + ' .body .commentEditorDiv').hide();
		$(_self.comment_id + ' .comment-' + edit_comment_id + ' .body .comment-text').show();

		_self.repositionEditors();
	};

	this.saveCurrentAnswerComment = function (CommentID) {
		$(_self.comment_id + ' .answerEditorForm #currentAnswerComment').attr('value', CommentID);
	};

	this.showAnswerEditor = function () {
		var comment = $(_self.comment_id + ' .answerEditorForm #currentAnswerComment').attr('value');
		
		_self.isAnswerEditorVisible = true;			
		$(_self.comment_id + ' .treenode-dummy').hide();	
		$(_self.comment_id + ' .treenode-dummy.treenode-' + comment).css('display', 'block');
		_self.resizefunc_answer = function() {
			$(_self.comment_id + ' .answerEditorForm').attr('style', '').css($(_self.comment_id + ' .treenode-'+ comment +' .ui-treenode-children .commentEditorDivAnswer').last().offset()).css('position', 'absolute');
		}
		_self.repositionEditors();

		_self.scrollToIfNotInView($(_self.comment_id + ' .treenode-dummy.treenode-'+ comment));
	};

	this.submitAnswer = function () {
		_self.isUserTyping_timestamp = 0;
		
		_self.isAnswerEditorVisible = false;
		var edit_comment_id = $(_self.comment_id + ' #currentAnswerComment').attr('value');
		
		$(_self.comment_id + ' .answerEditorForm').attr('style', 'height: 0px; overflow: hidden');
		$(_self.comment_id + ' .commentAnswerDivWrapper').css('display', 'none');
	};

	this.cancelCreateAnswer = function () {	
		_self.isAnswerEditorVisible = false;			
		$(_self.comment_id + ' .treenode-dummy').css('display', 'none');
		$(_self.comment_id + ' .answerEditorForm').attr('style', 'height: 0px; overflow: hidden');

		_self.repositionEditors();
	};

	this.deleteComment = function (comment) {
		$(_self.comment_id + ' .comment-' + comment +' .body .comment-text').html('<i>This comment was deleted by the Author</i>');
		$(_self.comment_id + ' .comment-' + comment +' .body footer .action.lockondelete a').addClass('clickedLink');
	};

	this.findIDbySelector = function (selector) {
		return $(selector).first().attr('id');
	};

	this.updateNodes = function (tree, c) {
	    var a = tree;
	    if (tree.cfg.dynamic) {
	        if (tree.cfg.cache && c.children(".ui-treenode-children").children().length > 0) {
	            tree.showNodeChildren(c);
	            return
	        }
	        if (c.data("processing")) {
	            PrimeFaces.debug("Node is already being expanded, ignoring expand event.");
	            return
	        }
	        c.data("processing", true);
	        var b = {
	            source: tree.id,
	            process: tree.id,
	            update: tree.id,
	            formId: tree.cfg.formId
	        };
	        b.onsuccess = function (j) {
	            var g = $(j.documentElement),
	                h = g.find("update");
	            for (var e = 0; e < h.length; e++) {
	                var l = h.eq(e),
	                    k = l.attr("id"),
	                    f = l.text();
	                if (k == a.id) {
	                	_self.initDiffNodes();
		                
	                    c.children(".ui-treenode-children").html(f);

	                    _self.indicateNewNodes();
	                    _self.reShowEditors();
	                    
	                    a.showNodeChildren(c)
	                } else {
	                    PrimeFaces.ajax.AjaxUtils.updateElement.call(tree, k, f)
	                }
	            }
	            PrimeFaces.ajax.AjaxUtils.handleResponse.call(tree, g);
	            return true
	        };
	        b.oncomplete = function () {
	            c.removeData("processing")
	        };
	        b.params = [{
	            name: tree.id + "_expandNode",
	            value: a.getRowKey(c)
	        }];
	        if (tree.hasBehavior("expand")) {
	            var d = tree.cfg.behaviors.expand;
	            d.call(tree, c, b)
	        } else {
	            PrimeFaces.ajax.AjaxRequest(b)
	        }
	    } else {
	        tree.showNodeChildren(c);
	        tree.fireExpandEvent(c)
	    }
	};

	this.initUserTypingEvents = function () {
		$(_self.comment_id + ' .answerEditor iframe').contents().find('body').keyup(function() {
			_self.isUserTyping_timestamp = new Date().getTime();
		});
	};

	this.dropTypingUser = function (type, typingContainer, user) {
		var $typingContainer = typingContainer;

		var $typingLi = $typingContainer.find('.hiddenUsersTyping');
		var typing_array = $typingLi.html().split('|');

		var tmp = new Array();

		tmp.push('{}');

		for(var k=0; k < typing_array.length; k++) {
			if (type == 0) {					
				if (JSON.parse(typing_array[k])['user']) {
					if (JSON.parse(typing_array[k])['user'] != user) {
						tmp.push(typing_array[k]);
					}
				}
			} else if (type == 1) {
				for( var k=0; k < typing_array.length; k++ ) {
					if (JSON.parse(typing_array[k])['date']) {
						if (new Date().getTime() - JSON.parse(typing_array[k])['date'] <  5*1000) {
							tmp.push(typing_array[k]);
						}
					}
				}
			}
		}

		$typingLi.html(tmp.join('|'));

		var typingcount = tmp.length - 1;
		if (typingcount > 0) {						
			var typing_message = (typingcount == 1) ? 'One typing User' : typingcount + ' typing Users';
			$typingContainer.find('a').html(typing_message);
			$typingContainer.show();
		} else {
			$typingContainer.hide();
		}
	};

	this.dropOldTypingUsers = function () {
		var $typingContainer = $(_self.comment_id + ' .typingUsers');

		dropTypingUser = _self.dropTypingUser;
		
		$typingContainer.each(function() {
			dropTypingUser(1, $(this));
		});
	};

	this.timer = function () {	
		var isUserTyping = (new Date().getTime() - _self.isUserTyping_timestamp) < 1000;

		_self.dropOldTypingUsers();				
		
		if (isUserTyping) {										
			$(_self.comment_id + ' .rcUserTypingForm').find('[name=\'currentAnswerComment\']').val($('.answerEditor').closest('form').find('[name=\'currentAnswerComment\']').val());

			_self.rcUserTyping_callback = function() {
				setTimeout(_self.timer, 1500);
			};

			rcUserTyping();					
		} else {
			setTimeout(_self.timer, 1500);
		}
	};	

	this.isOnFirstPage = function () {
		if ($(_self.comment_id + ' .cbpaginagtion button.ui-state-disabled').length == 0) {
			return true;
		}
		
		return $($(_self.comment_id + ' .cbpaginagtion button.ui-state-disabled')[2]).find('span').html() == '1';
	};

	this.showNewRepliesText = function () {
		$.each(_self.newAnswers, function(i, val){
			var $updateAnswersPanel = $(_self.comment_id + ' .comment-' + i + ' .updateAnswersPanel');

			if (val > 0) {
				var new_reply_message = (val == 1) ? 'Show One new reply' : 'Show ' + val + ' new replies';							
				$updateAnswersPanel.find('.getReplies').html(new_reply_message);
				$updateAnswersPanel.fadeIn('slow');
			} else {
				$updateAnswersPanel.hide();
			}
		});
	};

	this.handlePushMessage = function (msg) {		
		var json = JSON.parse(msg);
		
		if (json.a) {
			if (json.a == 'NC') {
				if (_self.isOnFirstPage()) {
					var $hiddenNewComments = $(_self.comment_id + ' .hiddenNewComments');
					var newCommentCount = parseInt($hiddenNewComments.html()) + 1;
					$hiddenNewComments.html(newCommentCount);
					
					var new_comments_message = (newCommentCount == 1) ? 'Show One new Comment' : 'Show ' + newCommentCount + ' new Comments';							
					$(_self.comment_id + ' .updateRootButton').fadeIn('slow').find('.ui-button-text').html(new_comments_message);
					_self.repositionEditors();
				}
			} else if (json.a == 'AC') {
				if (json.u == userid) {
					return;
				}

				if (_self.newAnswers[json.p]) {
					_self.newAnswers[json.p] = _self.newAnswers[json.p] + 1;
				} else {
					_self.newAnswers[json.p] = 1;
				}
				
				_self.dropTypingUser(0, $(_self.comment_id + ' .comment-' + json.p + ' .typingUsers'), json.u);
				_self.showNewRepliesText();						
			} else if (json.a == 'T') {				
				if (json.u == userid) {
					return;
				}
				
				var $typingContainer = $(_self.comment_id + ' .comment-' + json.c + ' .typingUsers');
				var $typingLi = $typingContainer.find('.hiddenUsersTyping');
				var typing_array = $typingLi.html().split('|');

				var obj = {};
				obj['user'] = json.u;
				obj['date'] = new Date().getTime();

				var found = false;
				for( var k=0; k < typing_array.length; k++ ) {
					if (JSON.parse(typing_array[k])['user'] == json.u) {
						found = true;
						break;
					}
				}

				if (!found) {
					typing_array.push(JSON.stringify(obj));
				}

				$typingLi.html(typing_array.join('|'));	

				var typingcount = typing_array.length - 1;
				if (typingcount > 0) {						
					var typing_message = (typingcount == 1) ? 'One typing User' : typingcount + ' typing Users';
					$typingContainer.find('a').html(typing_message);
					$typingContainer.show();
				}
			}
		}
	}
}