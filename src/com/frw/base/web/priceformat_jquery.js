/*

* Price Format jQuery Plugin
* original By Eduardo Cuducos
* Modified heavily by Juliano Viana

*/

(function($) {

    $.fn.priceFormat = function(options) {

        var defaults = {
            prefix: 'US$ ',
            centsSeparator: '.',
            thousandsSeparator: ',',
            limit: false,
            centsLimit: 2,
            acceptsNegative: false
        };

        var options = $.extend(defaults, options);

        return this.each(function() {

            // pre defined options
            var obj = $(this);
           

            // load the pluggings settings
            var prefix = options.prefix;
            var centsSeparator = options.centsSeparator;
            var thousandsSeparator = options.thousandsSeparator;
            var limit = options.limit;
            var centsLimit = options.centsLimit;
            var acceptsNegative=options.acceptsNegative;


            var input_mask;


            var input_mask_regexp="[0-9]";

            if(acceptsNegative)
                input_mask_regexp="\\-{0,1}"+input_mask_regexp;
            

            if(limit!=null && limit!="")
                input_mask_regexp+="{0,"+limit+"}";
            else
                input_mask_regexp+="*";
            if(centsLimit==null || centsLimit=="" || centsLimit==0)
                centsLimit=2;

            if(centsSeparator!=null && centsSeparator!="") {
                input_mask_regexp+="("+centsSeparator+"[0-9]{0,"+centsLimit+"}){0,1}";
            }
            input_mask_regexp="^"+input_mask_regexp+"$";

            input_mask= new RegExp(input_mask_regexp);


            
            function convertCharcode(charCode) {
               // alert(charCode);
                
                if(charCode==188)
                    return ",";

                if(charCode==109 || charCode==189)
                    return "-";


                return String.fromCharCode(charCode);
            }
            // filter what user type (only numbers and functional keys)
            function key_check (e) {

                var code = (e.keyCode ? e.keyCode : e.which);
                var typed = convertCharcode(code);
                var functional = false;
                var str = obj.val();
				

                // allow keypad numbers, 0 to 9
                if(code >= 96 && code <= 105) functional = true;

                // check Backspace, Tab, Enter, and left/right arrows
                if (code ==  8) functional = true;
                if (code ==  9) functional = true;
                if (code == 13) functional = true;
                if (code == 37) functional = true;
                if (code == 39) functional = true;
                if (code == 46) functional = true;
                               
                if (!functional) {
                    e.preventDefault();
                    e.stopPropagation();
                    var plainStr=str.replace(/\./g, "","");
                    var newValue=plainStr+typed;
                    if(newValue.match(input_mask)) {
                        if(thousandsSeparator!=null && thousandsSeparator!="")
                             newValue=addThousandsSeparator(newValue,thousandsSeparator,centsSeparator);
                        if (str!=newValue)
                            obj.val(newValue);
                    }
                }

            }

            function addThousandsSeparator(val,separator,centsSeparator) {

                var val2="";
                var i=0,j=0;
                var negative=false;
                if(val.charAt(0)=="-") {
                    negative=true;
                    val=val.substring(1);
                }

                var centsIndex=val.indexOf(",");
                

                for(i=val.length-1,j=0;i>=0;i--) {
                   
                   
                    val2=val.charAt(i)+val2;
                    if(centsIndex==-1 || i<centsIndex)  {
                        j++;
                        if( j>0 &&  j%3==0 && i>0) {
                            val2=separator+val2;
                        }
                      
                    }
                     
                        

                }
                if(negative)
                    val2="-"+val2;
                

                return val2;

            }

			

            // bind the actions
            if(!$(this).data("priceFormatBound")) {
                 $(this).bind('keydown', key_check);
                 $(this).data("priceFormatBound",true);
            }
       

        });

    };

})(jQuery);

