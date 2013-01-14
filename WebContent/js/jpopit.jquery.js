/**
 * jPopIt
 * Unobtrusive popup message system for use with
 * jQuery.
 *
 * Copyright (c) 2011 Shazy Productions
 * Version: 2.0.0
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * http://facebook.com/ShazyProductions
 */
 
 /*global jQuery, $, console */
 
(function ($) {

	if(!$.spro) {$.spro = {};}
	if(!$.spro.jpopit) {$.spro.jpopit = function(content, sticky, position, params) {
	if(typeof sticky === "object") {params = sticky;}
	(!params) ? params={} : function(){/* params is defined so don't create a new object */};
	
		//	Initialize jPopIt.
		$.spro.jpopit.init = function(debugMode) {
			$.spro.jpopit.options = {
				debug: $.spro.debug || false,
				
				trace: function(msg) {
					if($.spro.debug) {
						if($._trace) {
							$._trace(msg);
						} else if(console && console.log) {
							console.log(msg);
						} else {
							try {
								throw new Error(msg);
							} catch(e) {
								alert("(jQuery Trace plugin not found and could not throw a new error...)\n\n" + msg);
							}
						}
					} else {
						return msg;
					}
				},
				
				place: "holder",
				count: 0
			};
			
			// Insert css template for the jpops
			$('<style type="text/css">' +
			'#_topPopContainer {' +
				'width: 10000%;' + /* Set to 100% or less if you want the pops to stack */
				'padding-left: 10px;' +
				'padding-right: 10px;' +
				'position: fixed;' +
				'top: 0;' +
				'left:0;' +
				'overflow: hidden' +
			'}' +			
			
			'._popContent {' +
				'color: white;' +
				'width: 350px;' +
				'opacity: 0.8;' +
				'filter:alpha(opacity=80);' +
				'float: left;' +
				'margin-top: 5px;' +				
				'margin-left: 5px;' +
				'background-color: black;' +
				'backgroundColor: black;' +
				'cursor: default;' +
				'-moz-box-shadow: 0px 0px 5px #000;' +
				'-webkit-box-shadow: 0px 0px 7px #000;' +
				'box-shadow: 0px 0px 7px #000;' +				
				'-moz-border-radius: 2px; ' +
				'-webkit-border-radius: 2px; ' +
				'border-radius: 2px;' +
				'overflow: none;' +
			'}' +
			
			'._popContent:hover {' +
				'opacity: 0.95;' +
				'filter:alpha(opacity=95);' +
				'outline-style: solid;' +
				'outline-width: 1px;' +
			'}' +
			
			'._popTextContainer {' +
				'font-family: Arial;' +
				'font-size: 15px;' +
				'max-width: 150px;' +
				'overflow: hidden;' +
			'}' +			
			
			'._popTextContainer a {' +
				'color: yellow;' +
				'text-decoration: none;' +
			'}' +
			
			'</style>').appendTo("head");
			
			//	TOP
			$("body").append('<div id="_topPopContainer"></div>');
			
			//	BOTTOM
			$("body").append('<div id="_bottomPopContainer"></div>');
			$("#_bottomPopContainer").css({
				"width": "1000%",
				"padding-left": "10px",
				"padding-right": "10px",
				"position": "fixed",
				"bottom": 0,
				"left": 0,
				"overflow": "hidden"
			});
			
			//	LEFT
			$("body").append('<div id="_leftPopContainer"></div>');
			$("#_leftPopContainer").css({
				"width": "350px",
				"position": "fixed",
				"top": 75,
				"left": 10
			});
			
			//	RIGHT
			$("body").append('<div id="_rightPopContainer"></div>');
			$("#_rightPopContainer").css({
				"width": "350px",
				"max-height": "1px",
				"position": "fixed",

				"top": 75,
				"right": 10
			});
			
			// CENTER
			$("body").append('<div id="_centerPopContainer"></div>');
			$("#_centerPopContainer").css({
				"position": "fixed",
				"top": "50%",
				"left": "50%",
				"margin-top": "-50px",
				"margin-left": "-100px"
			});
		};
		
	if(!$.spro.jpopit.options) {$.spro.jpopit.init(true);}
		var log = $.spro.jpopit.options.trace,
			  count = $.spro.jpopit.options.count,
			  contentType, template, element;
		
		//	Check what is being passed to us, if anything at all.
		if(content) {
			(content.search(/^#(\w)*$/) === -1) ? contentType = "string" : contentType = "element";
			
			if(contentType === "element") {
				log(content);
				content = $(content).html();
			}
			
			position = position || "center";
			params.fadeInTime = params.fadeInTime || 1000;
			params.fadeOutTime = params.fadeOutTime || 500;
			params.delay = params.delay || 5000;
			params.sticky = params.sticky || false;
			//params.font = params.font || "Arial";
			params.noIcon = params.noIcon || false;
			params.noClose = params.noClose || false;
			params.iconSrc = params.iconSrc || __geticon("warning");
			params.closeSrc = params.closeSrc || __geticon("close");
			params.callback = params.callback || function(e) {
				log(e);
			};
				
			var template = '' +
			'<table class="_popContent" id=pop' + count + '>' +
				'<tr>' +
					'<td style="{1}padding-right:10px">' +
						'<img class="_popIcon" src="' + params.iconSrc + '" >' +
					'</td>' +
					
					'<td style="" align="left" class="_PopTextContainer" width="100%">' +
						'<span class="_popText">' +
							content +
						'</span>' +
					'</td>' +
					
					'<td style="" valign="top" align="right">' +
						'<img title="close" id=popImg' + count + ' style="{2}cursor:pointer;" height="20px" src="' + params.closeSrc + '" >&nbsp;&nbsp;' +
					'</td>' +
				'</tr>' +
			'</table>';
		
			//Lets check for dependents
			(params.noIcon) ? template = template.replace(/\{1\}/gi,"display:none;") :  template = template.replace(/\{1\}/gi," ");
			(params.noClose) ? template = template.replace(/\{2\}/gi,"display:none;") :  template = template.replace(/\{2\}/gi," ");

		
			// Add the new jpop to the page, but instantly hide it
			// then have it fade in.
			$("#_" + position.toLowerCase() +"PopContainer").append("<div class='_popContainer'>" + template + "</div>");
			
			element = $("#pop" + count);
			element.hide().fadeIn(params.fadeInTime, function() {
				removeJpop();
			});
			
			// Add onmouseover so the element that the mouse
			// hovers over won't dissapear. Start time when mouse
			// leaves, however.
			element.mouseover(function() {
				element.clearQueue();
			});	
			
			element.mouseout(function() {
				removeJpop();
			});
		
			// Add onClick Handler on specific jpop to close
			$("#popImg" + count).click(function() {
				element.clearQueue().fadeOut(params.fadeOutTime, function() {
					element.stop();
					element.dequeue();
					$.data(element, "finished", true);
					params.callback(element);
				});
			});
			
			function removeJpop(e) {
				var _element = e || element;
				
				if(sticky !== true) {
					_element.delay(params.delay).animate({
						opacity: 0
					}, params.fadeOutTime, function() {
						if(!$.data($(_element), "finished") === true) {
						params.callback(_element);
						}
						_element.remove();
					});					
				}
			}
			
			// Add "1" to the counter (for the id future jpops)
			$.spro.jpopit.options.count += 1;
			
			// Return the created jpop (jquery object)
			return element;
		} else {
			log("Error: You need to at the least pass an element or a message. example: $.spro.jpopit(\"Hello\");");
		}
		
	};}
	
}(jQuery));

function __geticon(which) {
	switch(which) {
		case "warning":
			return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAABktJREFUeNrsWl1sFFUUPrOzM7Pbzu7Odn%2B6O822pVhq%2BbESxGgkUUjUGhWj4oMlRhMSzD7VNMGQrIFEy4vGN2PDg0%2BK6IN%2FBENQJD6IPhGpJJRqYiKEgOAWKt2f%2BffcC7S77Ozs7G7rloSbnGznzr3nnO%2Fe83fvlLEsC%2B7k5oE7vN0F0OrmLX2YzDB1Mxh88cmyZ17wwOSnR7bhnzuRHkUqIP2CNDH08lOHVNUsGz%2F1xdG6ZQ7ts%2BwBNNRKgsDUl9%2Fdgz9jobWpdGRjP4grEmBqBp8%2Fd3n4yomp4cmDRybw%2FVuDLzwxs3xMiABA4nm6e2Py5nXp3mc2QsDbBsyZGWD%2FmIWAT4S%2Blx6BzocG0jhmnOeY%2BXnLAgADFkx%2BdjQj9cnp2EAPwF85UK8UQS3qN%2BhvtKJzOUgM9UOgK5qe%2FPxoevkAMC0489WxkIdlx%2BX1qwCua6DmdNpfSir2A%2FbL6weAYZgPp74%2BxtJ3rQbgYagSu6MDKeBEAYw5VNQybcnKqeCLiBDuk%2BkcBsymATClmbiRKIRtgOW5s4PPbQKW8YCSMxwHC6IXNEWFs4dPgKkbEeyaaSYKLUYeSCfW9ALLs6CS1TdNR9JxDNfOQ6w%2FRXdhUfNAA22TIPpHI%2F1oEnkNrWTBJD75QYeJw9r885b7WXjnVR4M3CAv%2BnR8TTdk%2F7ywS1e0j%2FH16Vb5QLpzsAeI4an5hdX%2F5metTHnSjp8y4PivOn2v5jTwsAwkVvdQHq1y4m3%2BUPtIuCeG0QVX31gwk%2Bnz9s45fd6g7%2BlYnNPRl0Cf8BEAj7cCQFpe2wugG6AU9HJbrxLfr%2Bet%2BTFKXgcGf5Ore5vahUYB7AzGpS1iPEhXssJZqyUoyyofh3kh1BWG9o7A8%2Fj2lf8LAIuJaH%2BivwsAs6xSRLMgJlFK1fIT6S8ZR3dOMSA5kGp4FxoBsFtKdoBf8oOBZkAiz%2B0kS%2FY%2BIPqsirEm7mB7RxuEOsMPk1pqqQGEGA8znuxP0pXTVMM%2B3lcxoaBQOVYlPJAIT%2BT9PqnIlxLArlgqBpwPs2lOr5qsAj57ALTfZrw2p4OAyS0ik8QMe5YKwAqWYzPxVIQmLUNDACQr2ZFpb0Jy0H485VXQINEbw%2FzgyeDQ7qUAMBpPRYHlPJi0dMdyQQ7Z10MBwag6h1SwhHdnd5TKWmwAm3iBG411SbhSOpiGWaPmqWJCfPU5lCfyjmJY5XgvceYHFxNAOpHqoImnSGoe3HYnSgY1WybJgPNcwtuDZTeR5TasugGw1d%2FGj0hREW1fLysZqjoxZ7h24FKivFFGOBYAlPkakb0YANJJ3FYGV6hQNGoqQQHwNgAE09VcIoPIIjLd7EItADsDAd9wIOQDM2%2BTcR2IKlwRgdzNJbKITCL75vVMQwCwZID9ySTWOxjmFIVEHsM1UYVLj20x1fVcKgtlEtlEB6JLIwDGJFwFfxsLeo2waUebV%2BbLmD3QVahrPpFJZEshv%2BPJrdqJzI8F27uJmEjTvKrVf3swMjQLt87bclCHDXKBnu3dNnKB51UtSHaKcG22OI68PsDuWbcAMtGwD3iBAWWObGtjNff2%2B66VXL%2FUP1%2FJk0sAD8Q6fHA5W9hFbvXcAOhmWSbTiQCgQNJ8674fkEoDChbEJR9krxUzhmGR8%2FN0LQBj8ZAALGtBcc6htq%2FR9p%2BMwIHT4fnn1zdkYfu6q3XzKaLr%2BEQ8P%2BOCXvinQMLqG05OnPCyzGgsgBVt0XRRMtjTgd%2BkMuVvATp0NlA3L6oD6hIROeC9HlIjrXMCsCPs9wJjma6Tlh1dvG7vWr9nhYb40eSGOkXbvRXJ7XYA29qwIiQHdXrHYzVKVYo5Tm%2BIH9VFM0BgqbpbnQCswPWHomJVXM7WQ8%2ButLf1x1L%2FNswzX7RuOWzCyYlnDcMIMRbTVPRYJeXg4NPT8OP5UInysyCLal254Pam3yjTLzkB%2BD6vmTuCfPNXpnK7AiP3Xq68lWiioW7k51snABNXFWOHyJJr8%2BX1Mc9A8Nkira8mnHzgJIJ87yJJYKa18BmoxUR0uVQwCYh9qOOpWonsTdS%2FcC5v7pE4BrCeAmJRrdgQhaQAXPQZrAZQ%2Bbexa6%2FbWmgvTvgoq1rp7I2L19WkwGsBBrLaP900mzM1v9Dcie3uvxrcBdBk%2B0%2BAAQAQhEkuM5YU1AAAAABJRU5ErkJggg%3D%3D";
			break;
			
		case "close":
			return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAABlZJREFUeNrsmttvFFUcx38zZ2a3e2n31m5rCQgRxEtAFp%2FEGASCEkMgXh4w%2FgX6bEybbWJilmwTfdb%2FwMSYaGqMQRQRffDBSNUHQSBAqgK9bHe7l2535%2Bb3nO0uhe5lZraLknCSyUxmZ37z%2BZz7Oa1kWRbdz0mm%2Bzw9EPivk%2BLmpbP7N%2FlwGjlw7p9rSx8kugIIvT3N423DZRHx5nteAvjYa7IiLff1K1dxnWSy5Bqev8tj8FhqH5vD9Zs9FRDwTPp097Fh2vv6KPkGlNTPU7NJ7sB7MycHf4e%2Fy2PsPTFKiVdHSO2TP3QqIdntRhH4ZcB%2FtvvwIEViHnGvolk0fWqOynl94umj8ZOGadnO%2BV%2B%2BnBPwiSNx8iq1UiwtGyKetmK%2Bher00YYJ1OF3HYxRNKZSqaiTbqL%2BBhhVLImmv56nckGf2PvSUEcJDn%2F%2Bq%2Fmkrx%2FwLw6RV7JoqWSIEukPKFRaMWn69LxtiY4CDfgDgI8qVAR8RbcaMGE%2FJHA9%2FU1GSCSODLaU4M9Pn1qowR%2BOkRf3csj1%2BvMqk2igLvHtgi2JtgKAPwb4qV37IxSNqFQo3YZfCxWpS5xZFBJ7Xoitk%2BDP%2FXo6U4M%2FFBXw2TXw9cQlQnWJMxnSKu0lWgo04J8NI%2BdVyjeBXwsXrUuchUTRmHjqULQhwX%2F%2F7cxi0hdkqcSBGvxiE%2Fi1ErxkS1VLxGsn0VSgAb8vhJxXUEdbwzckUIdjyLkKoZqcywqJ3c%2BHTvLffv9%2BqQaPkvSSRRnEMzo0PS7BS7ZUgcQPuZYS6wQA75Fkqjy5J0hDwx5RzBXDZu%2ByKqFxiR9zVC4ZE%2Fy%2BLwD458Kk2oRvSPA25mOUW9LpwvkCaVUrfvdg12wkHlFUmcr42M2c5mhQ0XEs5DWK%2BRVK7Bug6Z%2FyKX4%2F8cwAKei2Fpbtw%2FNUxcNzmglwk1Zf89itQmOqV05vftxHqsf5SCtKAhIGq73LAJJxCF9PqDo0c6FMetV6B7n%2FvpNGPKZ4pPTmnX2kuJSQpdp7Jr7hDt6iv%2F9cIV1rDm%2BnGxUSm7Z7XUl0k1Df6cblSlt4uwPZmKJK6dFHVML5nsDrHP6q1hHeyVRCSIw8rPRcAtB08zrai94Z3ulkbkxRKB3fAgmld%2FCzf2GA08kWvCOBugSDxPAmRkzZaHiiuRvO4B0LrJUYGpY2TALQNH8LPZXhDN6VQEOCUToWR3fJuofPYGx1A%2B96TYwUVAAewtvLGGENl1tLfKwIKjLlZCEQdBPDTRVKBbyUfGJUogKG%2BRyGedOlAF%2FERDwyBSDxxw2Llqv0Hkrh3Z4JCHgPJR%2BLm1TA7LQb%2BPUSEl2YxRxMcybhpBtN%2BVUruTNmUpHDa93Dr5NAnbq4wKis25ewO5Cl%2FIqV3BHRNxy%2BmcSlrAIJyZaEnalEysfh%2BzXKo8EuYbAxe7QfzCXCmMoHUZ0u51VaMTpLdJrMpXzMTG4PVHsOf6eEJHqnK0UvrZjtJdpNp1M%2B2Uxu66ugwa7C38MN2zBmvwEm07UVLiG3lGi1oEn1AX6rWu4K3lrd%2BJNcvC0kUBK8i71e9VHFai7RbE28k5F50a8XyYsoZcMlPF9Ys4C49holfMidhB%2BNGusaKrIgOOQdkLjSaSSeMZBnFZORpmuuqgCHr3oC%2FDwutiBZIO2pFh1LGDgKPANllUwmSnOm4%2BYuDMsomONVjx%2BLdIVMHsDBYZgS4IMC3lPKTvKDX%2FN7%2FDen8TgDzwyko2CrOtrYQl5OKeUCSTZLwpJlMnwhfh5XC5nJO5aI%2FbExyTTTrLxEONuLp6ik%2B%2Fo55nHAf%2BFqa5Esa4otQ0KrdqiwgA9yeDau5BYmm875w4NjBAmlmMNK3%2BwIbwQGQNga3u5AJiTkYr61BIcfCOPMxtni%2FGTbeh0dEhIsn20pYakeMoMC%2FhXAf74RUwkhIRVQ%2FNW7JGT0WaEwXxiMywtzk3aqhjkYh4SRlnO8JIy7tuPQYHlm2IB3OpmrSSzho3UJrGascETAS3Ozk456qvjwGBYBaSmXFYuBGrwH8QT8CcB%2F0ovptJAgnnM6PhqtwdMtZ%2FC3NzFrErSY5VvYmM1FHMG7XdDUJHj9Bbx185Y7%2BDrAQyM1CbQjwL8B%2BI%2FvxZqY%2F5l1CB%2Bb%2Be7R7hbFBy8ZPN4WXC4iXrHnS8r%2FW3rwrwYPBLpM%2FwowAJg8MNlqkTxjAAAAAElFTkSuQmCC";
			break;
			
		case "info" :
			return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAADlxJREFUeNrUWllsHOd9n2%2Fu3dmLpEnxEkWKEqnQsqwojmUlCNq6ddSkfWrRI0jT68EvLYIgQfISoC99c4C%2BJkDgPAXI8VCgSAK0TZu2QBvXsnzJsmxR4iGSy3uv2d25j%2Fz%2BuzvL2eVwKbl9yQB%2FcfY7f%2F%2F7%2F30jlv7i17kneeb%2B%2BhvH2ta%2B94qCP1OgaVABlAaNgvjOkBB0AGqCqqAtUBFr2dwTPl9%2B%2BI89v9mTMhB7RNB50CITxLNSfriujJzRpcKIJWZybnrmQhPtYQu97zFj46HmNXTJrZYUu7SXd2vlLNo30X0ftAryThLQIAbEjwj8GuP5a%2FLImap2bqGUf%2Bb5NxJHhmFbSrwQarOL9VjPDv1Te%2B9Woflo%2BRNOae%2FFMAjeQtNbmOMN2vzll1%2F%2BP2ngCmP8C6mpc4fDz%2F3mDqTucv8PD7QhlW%2F%2F57hZfDQahsH%2Fzv3F1%2B6cNPbuS2c%2BEgM5Ai8Xnpoavv7ipnpm%2BkTb1e%2B9NeUZ9ZxvNAqh77c0zATBE9KZqpjO6rmla8WT5lp7W2r59V9MO9VDGnNn9s%2B%2FqvePef%2Bz40%2FMwAzH2EuZ8x%2FbfupTn91LBP3h21Pm1vqSbxlTYq5gC6pmS9mcxUQpaFmS5%2FJuXVd9q6l4ekVF%2F1ZqevZe7tLHE5k5%2FOW%2FjjdWP5iAOf189ktf2ehh4ObkEzHwNOz3ucLHP72Wu3Q1kgaLLLx299astbv1NC%2FJhfTs4n5q8lxdUNN%2BJ%2BqwWATqPoFtiWZxLddcvz8auE4V2rwLH3rUty%2FTP3wnW337f%2BbCwH%2Fz3Be%2FfDfquPe56Z6BgnTl0yeBX%2BJl5crYb%2Fz%2BA23mYrPlkB1yyvup8hv%2F9bzbqF3NLV4tF569UYR5Gbwg%2BugPO8TF3rsEc%2FJpLJz6kJdlwdhcecbYWMlIWu5QSKXdaB4imqOMTVbA7KXqO78MCpc%2FeUB9f7OQfywGLkKqV0c%2F8%2FlVLGK3hdim%2Bv13Jqrvvf6H6pkpb%2BT6i2tSfsjscBaNCQPHFJ3KQdbaLw7BnnOB5%2FBMFDyAD7gjSXBSbshMn7t44NYrY7V7t18IXHdfeeqMHq0jallPGRnXza3VS9V3X3Pyl58r%2F%2B1C4VQGhhD3PjPywm%2BvpsbPWnHJ1x%2B8N1Zfufe5%2FNPPreUWrxww2ikm3eajBxP1D9%2B50lxbvgJbH4cTFwLbHLH3izNoW7R2N0dCz%2FPk3FA90hKtoY5N6bysmvryu9fQVlSGRrsaF9MZD35VN7bWlmCy29%2F8g5es0%2FLAfGb%2BY1upiRkD9ncU6ho12dxeXwTwNe3sfBXgun1GcXXc2tmcg4RzmblLh8rY1EGSWsGIhri%2FZO1snFXHz66mp8%2FvRn14L8MnOGPz4SK0XpYy%2BShEh4QFmIqNh%2B%2FP4%2FftQRpYgFqnRm%2B8tBuXPFHlzf9%2BXs4P5bMXn9nnwiCMqH7%2F3XnE72exQZX6EC6deH%2BcqC81ca7KRFFoPLx3CaHWl4dHy%2BjjqF%2FODxtevTJibKwW0lNzmzFf4lJnpk2zuD7x9z%2F4CQ8tlCLAfJwZmM7ThSvXdyD5ME7VO6%2BfxwaTiETFeHt95f1Zu3qwULh6Y015alxHaRB0KOyjnnaMrdMcmktrxNekPWgv2rMfB2EjjNqffUNIYuCCOjZRgkSs2KSAyC7vXUjPLW5CSB5Mp0WoZ9L23tZSbuHqOkKngTY%2FRl4fHWunOTSX1qC1un3Yg%2FaiPTv7d3EQNsJIWPsZ4JGsLiIkHnIYCwo6FFbv3p4T0poAx2rEJBlAnefUiXO7YlozkaiCftI%2FeHvh8LWfv0hE70ljaC7W2KG14mvTXrQn7R3hiKiFkbELkRYiJ56A%2FVURtty447act3I4n714eR%2FSCaLk5DVqKd9oTmQvXF4FkDCe4BClLgHQ9fT89ELhxlLbyVc2ub1f%2FNNyamr29ez80gfx8eroZIUCAOqh%2B2Imb0Tt6cm5vfqDu2RGKz1RBxiBteZUS1RTFCMGxtWJmRqpqSdqlPbTcCANsX4r9N1udrV2N8alfOEA0nLCI3755saDi0hKX5r8ky9w2oVFLugEvMI1HAQe3l%2FY%2FtEPFuCR30ViXI6bL4JDidbU5i4tR3tgTx17T1gHO6oyMmbGszphjRigRSSEv3wrbMJM42TvFccELVtD7PZBQeev7zb0MSlb0EPPCWLkkeRHPnWTE%2BVxrvneKmfc%2B7BF9E5t1IcxNzA2pPERidl8ldaM70HvkLZOGEj7HWrhIqyEGWYkkQbyQipjgO2A6zMfFF9ZhNUG5%2FdqBhkzzUvKduh2q%2BnQ1StD0OAzoqpy9vY6GnoLVr9Z56gPYy47pX1at9JVnaSYWGsa%2B4Tx2klQUw2smwWunnqKkh9hhinniYGclMka%2FebTBuqkeAWLB54fa5N4wMN4L76qXS2NCUqKfGZgaUtjWmO17GEvqND2bYOhhOkeaGhvwpCEjTCDgRwxoIIbO%2FR7uWyJNQgZ45hLKu1uxHjoMvQCx%2FLx3p2DE5qOSpPz6tWBDNAYluf10HW8o4NbwLAmrWdhrxhT2DsI1SRshJmwEwMKbK0S45IdrYDpOOuFvcKmhY3QMhknSn7bs1iI%2BmYNtRCH4o3jRSkZvOdyntnkcrnCOhgIYu0iC1kzaAuKkdSQfLE3jpd4C49MqBtIgNmh%2BEAMCIKiepzfMyh6swLH8bBwzzkVat5xGroiZXJmj1qz%2BR9be8U%2FVnJDBOPY%2BdjWK60xge9DevE6S0%2BhtN6lHBAHEDg2SgnO7PPBsI1BIkxM7By6g3hxdmQWQgMqlzkfUSGmA2TRHbdZWxCVVA9j6fGZnza2VnJm5eB3RVmm%2BR1T9DnPcWDT6j9jzE86zt%2Fl0GvWZRRv63FT7ZgbrEpoJpkQ2v0okflhr5d333lRrKMcHu2YUBDFbjjQoaOXZxClJGijJ9xoEzPft8sHd9ym%2FjuBbZ9nZBGisAIT%2BzdlePROJ59EDOCnI2N%2FG2vuw5ai3NDaC3sLvCCU%2BvB1hULjiAHXt60QdnvcURApnPLBROj5XmzhdkZU0o%2Bcem1WyRSawNgzV84OvUl0bFPX6zcr5lIkUTVIv2UmQY%2FLWKaI%2BqcU%2BgkR0rGIWZcYcAKzwXgtd2yQkh%2Bu24c7tmc1mSDLXq%2B9Z7dx8lKdZjUvpTOlj3SdYjRGeJ7fpbXCoPeGxnccEeHbJgz9%2BaltXi33c4gB023UmaBqQU8E6siIl9RtV68W%2BMKwRZYXVyJqkmW7cjjn1OsFSU2VqdGpVz%2Fhu%2FbziB3DkPBIJ%2FZWYMwlQZJvydnCbbJS1zSGmSCW5cLIWtgyfToUROuDHb2a5eXUdtib3GJ%2B06B3ixiwfMtQOnZ2zIxgtzvGzqNxydWqjBePXWTBth86tfKM09RHPcv4U%2B3iws3CJ5c4MZ%2FlpKFc5%2BKqznkVnau%2Bce9m88Hyv4hq%2Bocw2UM5V3gU%2Bj2KbRs2qm3PbMja9PntviTWxYdwzEcMGGAgleTpbUeWPEFSig4kAunVksYgnOJwUlJQTd6c%2BL2%2F4pgKEAYqk0qbX1Ga5OSzPJcau86tv%2FrKTc61X8Wc1T7w3cdB%2BYDQvkGRJik6tsscgxgwWiaEWM9zFGnasTvsN6XU2GTR2N6YA9ciSgGnL6d0PM4bkXPDnLOzObiUUFFumM0s8mOQcJnKIaAoqPstHCF3k2yfa18ksBZmYCcGKNsakKAAc3CTLqPaNYyy7xrNswiKLl0VJqxrBB6Ow54zkAGcH1pjET%2BPoaOrSDh2WkxpW0mRJ6YhkTA3v%2F9KEJ0HGghnGag1PGkSqkfd99xtp1kfldR0rcNEXFM1ZE6Et8EM0BgaS0fMPvCCaxmocuVtCLIWJks%2FuiGhDFmPn8hM37bzPkITFjiRCaUwUrarhz6kNCoqSgNjnaMij1XpWgTxeTADKLMZz6pcDCHmyZ5tZ1B97mGPgeAxlgFry3ziDNDdhY5TjqIOjw0UoZIdqrpMd3zHygWupyI%2FmACPspXV2gzYp5iQRxm%2Bdcagm0bE%2BxSM2kdkKsICDM73B84HRomwwnzC%2Fostw7OtNEIhJ8hKUmnBonccxptEdq1ccCwjx%2FOCLYhiBbE9SjAnSxA%2BgDpJd21LCgJf5gVJR7KqxcqDxEDSSW4MGMl8jKSbOfLuOhwkrgXW59A9ZS3CagVS15HyNde26btY3TcaWSSoZOkHratRC4WdgPBsyFpmD1VlELYDEkuK9%2FHfqL8UzG9E0k%2B6WrQQDnlkU0HWsl7SIv2Lo9jyAIQkWDUO92q%2B0UQGVZIZaMd9U80PbXeF05Y6Owl09I7gIQKbA%2FDmaXejtmcaGiTEhGSHPsnJqb2EBDN90pCOfxzQGWmQpPvNBxUrA6au457GgN%2Buj3SFaVkXDjcIcP%2Bz7DX1ZxlLnuAZ9JWVW%2BeOly0nSh9SZy6kT5hShWH%2FcRhohVq6V4XaJCmtebyQyESSzb7rm8YfCSeYkGc06M%2BtmAYGPQw1ESrWpgAstporuGFC8h70mZWYgO01JFFRffG4OR0%2FJTH2Hyi9OTGVSmbApKMD%2B3cuCE5F7yEkdyKOo2Zy3klzTvtO7LW0iJBHxR6S10DJyWntlt1srASWOR8dJ%2BMRiPKEKCuvheFgDSCpMWR90rCtaBkqdQZ%2BtD7todkOFuQRt%2BHbcoBDyKDx34HavwWt9YJqO%2FDfCVSCnCBNqu8geb5jYo6SSoenaetxv9SH7bNzECIBAT8fCpJE2fe4FlT1HxzL%2Bi30fT5ilKSPSMLh97eSpE%2F%2B5rsuAwO0oI81gs590WP9t4EneYK2oAKGAztPF1sEElLtZ%2FbbkGSXgU7l%2FAVRlFDtheyofvMJdASU%2FgllCgCP5eMf8f9KSJLUPp%2B7Ln15oJKG%2BYgWrduoDoGhnwEYSoXgL0kpaHsVzDg0Lmw%2FLalzR582o3UfS%2Bo9gSMMQ%2B7X%2BfmVAAMAUNgg6LYvds4AAAAASUVORK5CYII%3D";
			break;
			
		default:
			return null;
	}
}