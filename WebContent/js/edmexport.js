
function Hash()
{
    var length = 0;
    
    this.add = function(key, val) {
         if(this[key] == undefined) {
        	 length++;
         }
         this[key] = val;
    };
    this.remove = function(key) { 
    	if (this.hasOwnProperty(key)) {
    		delete this[key];
    		length--;
		}
	};
    this.length = function() {
        return length;
    };
}


(function()
{
	ListCollectionsJS = function()
    {
        if (arguments[0]) this.itemspage = arguments[0];
        if (arguments[1]) this.pagecount = arguments[1];
        this.list_collections = new Array();
        this.list_collections_dict = new Array();
        this.list_collections_submit = new Hash();
    };//ListCollectionsJS

	
	ListCollectionsJS.prototype =
	{
			list_collections: null,
			list_collections_dict: null,
			list_collections_submit: null,
			list_num_collections: 0,
			itemspage: 10,
			pagecount: 10,
			numPages: 0,
			currentPage: 0,
			currentOffset: 0,
			
			setItemsPage: function(itemspage)
			{
				this.itemspage = itemspage;
			},
			
			setPageCount: function(pagecount)
			{
				this.pagecount = pagecount;
			},
			
			init: function()
			{
				jQuery("#div_list_collec_nosc").hide();
		    	var list_collections_jq = jQuery("#ul_list_collec_nosc li");
		    	var i = 0;
		    	var objListColl = this;
		    	var char_ant = "";
		    	list_collections_jq.each(function() {
		    		if (jQuery(this).find('input[type="checkbox"]').length > 0) { 
		    			var obj_collec = new Object();
		    			obj_collec.id = jQuery(this).find('input[type="checkbox"]').val();
		    			obj_collec.name = jQuery(this).find('input[name$=".name"]').val();
		    			obj_collec.handle = jQuery(this).find('input[name$=".handle"]').val();
		    			objListColl.list_collections.push(obj_collec);
		    			jQuery(this).find('input[name^="listCollections"]').attr('disabled', 'disabled');
		    			var firstChar = obj_collec.name.charAt(0).toUpperCase();
		    			if (objListColl.list_collections_dict[firstChar] == undefined) {
		    				objListColl.list_collections_dict[firstChar] = new Array();
		    				objListColl.list_collections_dict[firstChar]["offset"] = i;
		    				if (char_ant != "") objListColl.list_collections_dict[char_ant]["offset_end"] = i - 1;
		    				objListColl.list_collections_dict[firstChar]["count"] = 1;
		    				char_ant = firstChar;
		    			} else {
		    				objListColl.list_collections_dict[firstChar]["count"] += 1;
		    			}
		    			i++;
		    		}
		    	});
		    	jQuery("#div_list_collec_nosc").remove();
		    	if (char_ant != "") this.list_collections_dict[char_ant]["offset_end"] = i - 1;
		    	this.list_num_collections = this.list_collections.length;
		    	this.numPages = Math.floor(this.list_num_collections / this.itemspage);
		    	if (this.list_num_collections % this.itemspage > 0) this.numPages++;
		    	if (this.list_num_collections > 0) {
		    		this.renderPage(1);
		    	}
			},
			
			renderPage: function(page)
			{
				page--;
				if (page < 0) page = 0;
				if (page >= 0 && (page * this.itemspage) < this.list_num_collections) {
					this.renderList(page * this.itemspage);
				}
			},
			
			renderFromDict: function(char)
			{
				if (this.list_collections_dict[char] != undefined) {
					var dict_offset = this.list_collections_dict[char]["offset"];
					if (this.currentOffset < dict_offset && (this.currentOffset + this.itemspage) > dict_offset) {
						window.location.hash = "#" + char;
						this.renderListDict(dict_offset, char);
					} else {
						this.renderPage((dict_offset / this.itemspage) + 1);
					}
				}
			},
			
			renderList: function(offset)
			{
				this.renderListDict(offset);
				this.renderListColl(offset);
			},
			
			renderListDict: function(offset)
			{
				var str = "";
				var firstChecked = false;
				for (var char in this.list_collections_dict) {
					var dict_offset = this.list_collections_dict[char]["offset"];
					var dict_offset_end = this.list_collections_dict[char]["offset_end"];
					if (offset < dict_offset || offset > dict_offset_end || firstChecked) {
						str += '<a href="javascript://void();" onclick="objListCollectionsJS.renderFromDict(\'' + char + '\')" title="' + char + ': ' + this.list_collections_dict[char]["count"] + '">' + char + '</a> | ';
					} else {
						str += char + ' | ';
						firstChecked = true;
					}
				}
				var div_list_collec_dict = jQuery("#div_list_collec_dict");
				div_list_collec_dict.empty();
				div_list_collec_dict.html(str.substring(0, str.length - 2));
				
			},
			
			renderListColl: function(offset)
			{
				var page = Math.floor(offset / this.itemspage) + 1;
				this.renderListCollHeader(page);
				this.renderListCollFooter(page);
				this.renderListCollBody(offset);
				this.currentPage = page;
				this.currentOffset = offset;
			},
			
			renderListCollHeader: function(page)
			{
				var div_list_collec_header = jQuery("#div_list_collec_header");
				var num_coll = (this.list_num_collections < this.itemspage)?this.list_num_collections:this.itemspage;
				var str = "Pag: " + page + "/" + this.numPages + ". Col: " + num_coll + "/" + this.list_num_collections;
				div_list_collec_header.empty();
				div_list_collec_header.html(str);
			},
			
			renderListCollFooter: function(page)
			{
				var div_list_collec_footer = jQuery("#div_list_collec_footer");
				var str = "";
				var pageIni = (page % this.pagecount > 0)?page - (page % this.pagecount) + 1:page - this.pagecount + 1;
				var pageAux = pageIni;
				if (page > this.pagecount) {
					pageAux = pageIni - 1;
					str += '<a href="javascript://void();"  onclick="objListCollectionsJS.renderPage(' + pageAux + ')" title="Pag ' + pageAux + '">&lt;&lt;</a> ';
				}
				if (page > 1) {
					pageAux = page - 1;
					str += '<a href="javascript://void();" onclick="objListCollectionsJS.renderPage(' + pageAux + ')" title="Pag ' + pageAux + '">&lt;</a> ';
				}
				var pageEnd = (pageIni + this.pagecount > this.numPages)?this.numPages:pageIni + this.pagecount - 1;
				for (var i = pageIni; i <= pageEnd; i++) {
					str += (i != page)?' <a href="javascript://void();" onclick="objListCollectionsJS.renderPage(' + i + ')" title="Pag ' + i + '">' + i + '</a> ':" " + i + " ";
				}
				if (page < this.numPages) {
					pageAux = page + 1;
					str += '<a href="javascript://void();"  onclick="objListCollectionsJS.renderPage(' + pageAux + ')" title="Pag ' + pageAux + '">&gt;</a> ';
				}
				if (pageEnd < this.numPages) {
					pageAux = pageEnd + 1;
					str += '<a href="javascript://void();" onclick="objListCollectionsJS.renderPage(' + pageAux + ')" title="Pag ' + pageAux + '">&gt;&gt;</a> ';
				}
				div_list_collec_footer.empty();
				div_list_collec_footer.html(str);
			},
			
			renderListCollBody: function(offset)
			{
				var div_list_collec_body = jQuery("#div_list_collec_body");
				this.checkCollections();
				div_list_collec_body.empty();
				var limit = ((offset + this.itemspage) > this.list_num_collections)?this.list_num_collections:offset + this.itemspage;
				var ul = jQuery('<ul id="ul_list_collec" />');
				for (var i = offset; i < limit; i++ ) {
					var li = jQuery('<li id="li_' + i + '" />');
					var obj_collec = this.list_collections[i];
					li.append(i + ": " + obj_collec.name + " (" + obj_collec.handle + ")");
					var checked = (this.list_collections_submit[obj_collec.id] != undefined)?"checked='checked'":"";
					var checkbox = jQuery('<input type="checkbox" id="listCollections' + i + '.id" name="listCollections[' + i + '].id" value="' + obj_collec.id + '" ' + checked + ' />');
					li.append(checkbox);
					ul.append(li);
				}
				div_list_collec_body.append(ul);
			},
			
			checkCollections: function()
			{
				var list_collections_jq = jQuery("#div_list_collec_body li");
				var objListColl = this;
		    	list_collections_jq.each(function() {
		    		if (jQuery(this).find('input[type="checkbox"]').length > 0) {
		    			var li_id = jQuery(this).attr("id");
		    			var pos = li_id.substring(li_id.indexOf("_") + 1, li_id.length);
		    			var chk = jQuery(this).find('input[type="checkbox"]');
		    			var id = chk.val();
		    			if (chk.attr("checked") == "checked") {
		    				objListColl.list_collections_submit.add(id ,pos);
		    			} else if (objListColl.list_collections_submit[id] != undefined) {
		    				objListColl.list_collections_submit.remove(id);
		    			}
		    		}
		    	});
			},
			
			submit: function(message)
			{
				var div_list_collec_body = jQuery("#div_list_collec_body");
				this.checkCollections();
				if (this.list_collections_submit.length() > 0) {
				div_list_collec_body.empty();
					var div_list_collec_header = jQuery("#div_list_collec_header");
					div_list_collec_header.empty();
					div_list_collec_header.html("<h3>" + message + "</h3>");
					var div_list_collec_dict = jQuery("#div_list_collec_dict");
					div_list_collec_dict.empty();
					var div_list_collec_submit = jQuery("#div_list_collec_submit");
					div_list_collec_submit.empty();
					var div_list_collec_footer = jQuery("#div_list_collec_footer");
					div_list_collec_footer.empty();
				var ul = jQuery('<ul id="ul_list_collec" />');
				var i = 0;
				for (var id in this.list_collections_submit) {
						if (isNaN(id)) continue;
					var li = jQuery('<li id="li_' + i + '" />');
					var obj_collec = this.list_collections[this.list_collections_submit[id]];
					li.append(i + ": " + obj_collec.name + " (" + obj_collec.handle + ")");
						var checkbox = jQuery('<input type="hidden" id="listCollections' + i + '.id" name="listCollections[' + i + '].id" value="' + obj_collec.id + '" />');
					li.append(checkbox);
					var hidden_name = jQuery('<input type="hidden" id="listCollections' + i + '.id" name="listCollections[' + i + '].name" value="' + obj_collec.name + '" />');
					li.append(hidden_name);
					var hidden_handle = jQuery('<input type="hidden" id="listCollections' + i + '.id" name="listCollections[' + i + '].handle" value="' + obj_collec.handle + '" />');
					li.append(hidden_handle);
					ul.append(li);
					i++;
				}
				div_list_collec_body.append(ul);
				}
			},
	};
})();


function selectText(pos, term, id)
{
	if (pos < 0) return;
	var EDMXml = document.getElementById(id);
	EDMXml.focus();
    if (EDMXml.setSelectionRange)
    	EDMXml.setSelectionRange(pos, pos + term.length);
    else {
        var r = EDMXml.createTextRange();
        r.collapse(true);
        r.moveEnd('character',  pos + term);
        r.moveStart('character', pos);
        r.select();
    }
    var EDMXmlJQ = jQuery('#' + id);
    var heightPX= EDMXml.scrollHeight;
    var rowHeightPX = EDMXmlJQ.css('line-height').replace('px','');
    var numLines = heightPX / rowHeightPX;
    var avgCharLine = EDMXmlJQ.val().length / numLines;
    var rowTerm = Math.floor(pos / avgCharLine);
    EDMXmlJQ.scrollTop(rowHeightPX * rowTerm);
}







