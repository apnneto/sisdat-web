/**
 * InputTextMask component script used for mask/regexp operations.
 * Mask Individual Character Usage:
 * 9 - designates only numeric values
 * L - designates only uppercase letter values
 * l - designates only lowercase letter values
 * A - designates only alphanumeric values
 * X - denotes that a custom client script regular expression is specified</li>
 * All other characters are assumed to be "special" characters used to mask
 * the input component
 * Example 1:
 * (999)999-9999 only numeric values can be entered where the the character
 * position value is 9. Parenthesis and dash are non-editable/mask characters.
 * Example 2:
 * 99L-ll-X[^A-C]X only numeric values for the first two characters,
 * uppercase values for the third character, lowercase letters for the
 * fifth/sixth characters, and the last character X[^A-C]X together counts
 * as the eighth character regular expression that would allow all characters
 * but "A", "B", and "C". Dashes outside the regular expression are
 * non-editable/mask characters.
 */
var InputTextMask = {
	processMaskFocus: function(input, mask, clearWhenInvalid){
		// create an input mask and register it on the specified input (if it hasnt already been added by a previous call
		InputTextMask.createInputMask(input, mask, clearWhenInvalid);
		if(input.value.length == 0){
			// when the input value is empty populate it with the viewing mask and move the cursor to the
			// beginning of the input field
			var cursorPos = InputTextMask.getCursorPosition(input, input.value);
			input.value = input.mask.viewMask;
			InputTextMask.moveCursorToPosition(input, null, cursorPos);
		}
	},
	getEvent: function(e) {
		// get the event either from the window or from the passed event
		return (typeof event != 'undefined')? event: e;
	},
	handleEventBubble: function(keyEvent, keyCode){
		// this method ensures that the key enterned by the user is not propagated unless it is a tab or arrow key
		try {
			if(keyCode && (keyCode.isTab || keyCode.isLeftOrRightArrow)){
				// allow all tab/arrow keys by returning true- no further action required
				return true;
			}
			keyEvent.cancelBubble = true;
			if(keyEvent.stopPropagation){
				// prevent other event triggers
				keyEvent.stopPropagation();
			}
			if(keyEvent.preventDefault){
				// prevent the default event from firing. in this case it is propagation of the keyed input
				keyEvent.preventDefault();
			}
			return false;
		} catch(e) {
			alert(e.message);
		}
	},
	createInputMask: function(input, mask, clearWhenInvalid) {
		// if this input hasnt already registered its mask go ahead and do so now. This only needs to be performed the
		// first time the input is encountered when it gains focus. It will attach the MaskType object to the input object
		// add add all of the appropriate event listeners to ensure that the mask is applied
		if(!input.mask || input.mask.rawMask != mask){
			input.mask = new InputTextMask.MaskType(input, mask, clearWhenInvalid);
			// add the event listeners that will ensure that when the input contains an incomplete mask it will be remove.
			// Also, make sure that the keydown event is fired from this point forward thus invoking the mask format.
			if(input.addEventListener){
				// most doms
				input.addEventListener('blur', function(){input.mask.removeValueWhenInvalid();}, false);
				input.addEventListener('keydown', function(e){return input.mask.processMaskFormatting(e);}, false);
				if(window.opera){
					// in opera- need to ensure that the keypress event isnt interfering with this input mask
					input.addEventListener('keypress', function(e){return InputTextMask.handleEventBubble(InputTextMask.getEvent(e), null);}, false);
				}
			} else if(input.attachEvent) {
				// ie
				input.attachEvent('onblur', function(){input.mask.removeValueWhenInvalid();});
				input.attachEvent('onkeydown', function(e){return input.mask.processMaskFormatting(e);});
			} else {
				// other browsers that do not support dynamic event propagations
				input.onBlur = function(){input.mask.removeValueWhenInvalid();};
				input.onKeyDown = function(e){input.mask.processMaskFormatting(e)};
			}
		}
	},
	getCursorPosition: function(input, previousValue) {
		// gets the current cursor position (s=start, e=end) and creates/returns a new CursorPosition instance
		var s, e, r;
		if(input.createTextRange){
			// ie- need to capture the start/end cursor positions
			r = document.selection.createRange().duplicate();
			r.moveEnd('character', previousValue.length);
			if(r.text === ''){
				s = previousValue.length;
			} else {
				s = previousValue.lastIndexOf(r.text);
			}
			r = document.selection.createRange().duplicate();
			r.moveStart('character', -previousValue.length);
			e = r.text.length;
		} else {
			// other browsers
			s = input.selectionStart;
			e = input.selectionEnd;
		}
		return new InputTextMask.CursorPosition(s, e, r, previousValue);
	},
	moveCursorToPosition: function(input, keyCode, cursorPosition) {
		// moves a cursor position for the passed input element to the specified cursor position- because the
		// range cursor position is 1 indexed we add an additional space (unless the pressed key is a backspace)
		var p = (!keyCode || (keyCode && keyCode.isBackspace))? cursorPosition.start: cursorPosition.start + 1;
		if(input.createTextRange){
			// ie move- cursor to the index p
			cursorPosition.range.move('character', p);
			cursorPosition.range.select();
		} else {
			// other browser- move cursor to the index p
			input.selectionStart = p;
			input.selectionEnd = p;
		}
	},
	injectValue: function(input, keyCode, cursorPosition) {
		// inject the validated key into the input mask at the specified cursor position and return true on success
		var key = (keyCode.isBackspace)? '_': input.mask.getValidatedKey(keyCode, cursorPosition);
		if(key){
			input.value = cursorPosition.previousValue.substring(0, cursorPosition.start) + key + cursorPosition.previousValue.substring(cursorPosition.start + 1, cursorPosition.previousValue.length);
			return true;
		}
		// invalid key
		return false;
	},
	MaskType: function(inputTextElement, mask, clearWhenInvalid) {
		// this object instance is holds relative mask properties for a specified input element
		this.inputTextElement = inputTextElement;
		// designates whether or not the input value is cleared when its mask is incomplete and a blur event is triggered
		this.clearWhenInvalid = clearWhenInvalid;
		// holds the last validated key code
		this.lastValidatedKeyCode = null;
		// the mask value used to validate/mask valid input
		this.rawMask = mask;
		// the mask displayed in the input
		this.viewMask = '';
		// the string array of all the raw mask values (some indexes contain more than one char so we need to track this)
		this.maskArray = new Array();

                this.isOpenEnded=false;

		var mai = 0;
		var regexp = '';
		// cycle through the raw mask and perform view mask conversions
		for(var i=0; i<mask.length; i++){
			if(regexp){
				if(regexp == 'X'){
					// end of current regexp slot
					regexp = '';
				}
				if(mask.charAt(i) == 'X'){
					// current mask array index contains the complete regexp so we need to store it in the array
					this.maskArray[mai] = regexp;
					mai++;
					regexp = null;
				} else {
					// still in the middle of the regexp keep adding the current character to the regexp
					regexp += mask.charAt(i);
				}
			} else if(mask.charAt(i) == 'X'){
				// current slot is a regexp
				regexp += 'X';
				this.viewMask += '_';
			} else if(mask.charAt(i) == '9' || mask.charAt(i) == 'L' || mask.charAt(i) == 'l' || mask.charAt(i) == 'A') {
				// the current mask character is one of the predefined/reserved characters
				this.viewMask += '_';
				this.maskArray[mai] = mask.charAt(i);
				mai++;
                        }
                        else if(mask.charAt(i)=='+') {
                            this.isOpenEnded=true;


			} else {
				// just a regular char
				this.viewMask += mask.charAt(i);
				this.maskArray[mai] = mask.charAt(i);
				mai++;
			}
		}
		// the predefined/reserved characters need to be replaced with the viewing mask char that desigantes an editable value (underscore)
		this.specialChars = this.viewMask.replace(/(L|l|9|A|_|X|\\+)/g,'');
		this.getValidatedKey = function(keyCode, cursorPosition) {
			// validates if the passed key code is valid for the specified cursor position and returns the value if it is. otherwise, return false
                        var maskKey;
                        if(!this.isOpenEnded)
			  maskKey= this.maskArray[cursorPosition.start];
                        else {
                            if(cursorPosition.start>=this.maskArray.length)
                                maskKey=this.maskArray[this.maskArray.length-1];
                            else
                                 maskKey= this.maskArray[cursorPosition.start];
                        }

			if(maskKey == '9'){
				// only allow numbers at the specified slot
				return keyCode.pressedKey.match(/[0-9]/);
			} else if(maskKey == 'L'){
				// only allow uppercase letters at specified slot (convert if necessary)
				return (keyCode.pressedKey.match(/[A-Za-z]/))? keyCode.pressedKey.toUpperCase(): null;
			} else if(maskKey == 'l'){
				// only allow lowercase letters at specified slot (convert if necessary)
				return (keyCode.pressedKey.match(/[A-Za-z]/))? keyCode.pressedKey.toLowerCase(): null;
			} else {
				if(maskKey == 'A'){
					// only allow alpha-numeric values at the specified slot
					return keyCode.pressedKey.match(/[A-Za-z0-9]/);
				} else {
					// only allow values that are verified by the specified regexp at the specified slot
					return (this.maskArray[cursorPosition.start].length > 1)? keyCode.pressedKey.match(new RegExp(maskKey)): null;
				}
			}
		};
		this.removeValueWhenInvalid = function(){
			// removes value from the input element when the mask is incomplete
                    if(this.inputTextElement.value.indexOf('_') > -1){
				this.inputTextElement.value = '';
			}
		};
		this.processMaskFormatting = function(e) {
			// capture event (should be the keydown event)
			var onKeyDownEvent = InputTextMask.getEvent(e);
			// create the key code from the event.
			var keyCode = new InputTextMask.KeyCode(onKeyDownEvent);
			if(InputTextMask.handleEventBubble(onKeyDownEvent, keyCode)){
				// the pressed key is allowed to propagate- no mask injection required
				return true;
			}
			var v = this.inputTextElement.value;
			if(v.length === 0){
				// when the input value is empty populate it with the viewing mask
				this.inputTextElement.value = this.viewMask;
			}
			var cursorPos = InputTextMask.getCursorPosition(this.inputTextElement, v);
			if(!this.isOpenEnded && cursorPos.end == cursorPos.previousValue.length && !keyCode.isBackspace){
				// input cursor position is at the end of the mask- do not allow any more characters to be keyed
				return false;
			}
			// move the cursor position to the next slot that does not contain a mask character
                        var typedText=v.replace(/_/g,"");

                        if(typedText.length<this.maskArray.length) {
                            while(this.inputTextElement.mask.specialChars.match(RegExp.escape(cursorPos.previousValue.charAt(((keyCode.isBackspace)? cursorPos.start-1: cursorPos.start))))){
                                    if(keyCode.isBackspace) {
                                            // backspace needs to move the cursor backwards
                                            cursorPos.decStart();
                                    } else {
                                            // still moving cursor one space to the right
                                            cursorPos.incStart();
                                    }
                                    if(cursorPos.start >= cursorPos.previousValue.length || cursorPos.start < 0){
                                            // end of the mask- no more keys should be keyed
                                            return false;
                                    }
                            }
                        }
			if(keyCode.isBackspace){
				// need to go back one space to the left
				cursorPos.decStart();
			}
			// inject the key that was pressed into the input value mask
			if(InputTextMask.injectValue(this.inputTextElement, keyCode, cursorPos)){
				// when the injection is sucessful move the cursor to the next slot to the right of the injected key
				InputTextMask.moveCursorToPosition(this.inputTextElement, keyCode, cursorPos);
			}
			// because the pressed key is being injected we always need to return false to prevent duplicate
			// key injection
			return false;
		};
	},
	KeyCode: function(onKeyDownEvent) {
		this.onKeyDownEvent = onKeyDownEvent;
		// get the unicode value from the key event
		this.unicode = onKeyDownEvent.which? onKeyDownEvent.which: (onKeyDownEvent.keyCode? onKeyDownEvent.keyCode: (onKeyDownEvent.charCode? onKeyDownEvent.charCode: 0));
		this.isShiftPressed = onKeyDownEvent.shiftKey == false || onKeyDownEvent.shiftKey == true? onKeyDownEvent.shiftKey: (onKeyDownEvent.modifiers && (onKeyDownEvent.modifiers & 4)); //bitWise AND
		// TODO : need to get cap lock capture for onkeydown event
		//this.isCapLock = ((!this.isShiftPressed && this.unicode >= 65 && this.unicode <= 90) || (this.unicode >= 97 && this.unicode <= 122 && this.isShiftPressed));
		if(this.unicode >= 96 && this.unicode <= 105) {
			this.unicode -= 48; // handle number keypad
		}
		if(this.unicode >= 65 && this.unicode <= 90 && !this.isShiftPressed){
			this.unicode += 32; // handle uppercase
		}
                if(this.unicode==109 || this.unicode==189)
                    this.unicode=45; // handle minus sign in IE and Firefox

		this.isTab = (this.unicode == 9)? true: false;
		this.isBackspace = (this.unicode == 8)? true: false;
		this.isLeftOrRightArrow = (this.unicode == 37 || this.unicode == 39)? true: false;
		// capture the actual key for the passed key code
		this.pressedKey = String.fromCharCode(this.unicode);
	},
	CursorPosition: function(start, end, range, previousValue) {
		// holds the cursor position values
		this.start = isNaN(start)? 0: start;
		this.end = isNaN(end)? 0: end;
		this.range = range;
		this.previousValue = previousValue;
		this.incStart = function(){
			this.start++;
		};
		this.decStart = function(){
			this.start--;
		};
	}
};
// Add escape prototype feature to RegExp object
// text.replace(/[.*+?^${}()|[\]\/\\]/g, '\\$0');
// text.replace(/([\\\^\$*+[\]?{}.=!:(|)])/g, '\\$1');
if(!RegExp.escape) {
	RegExp.escape = function(text){
		var sp;
		if(!arguments.callee.sRE){
			sp=['/','.','*','+','?','|','(',')','[',']','{','}','\\'];
			arguments.callee.sRE = new RegExp('(\\' + sp.join('|\\') + ')','g');
		}
		return text.replace(arguments.callee.sRE, '\\$1');
	};
}