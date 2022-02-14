
function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getAllBrand() {
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	        displaySearchBrandCategory(data);
	   },
	   error: handleAjaxError
	});
}

function displaySearchBrandCategory(data) {
	var $brandBody=$('#searchForm').find('#enterInputBrand');
	var $categoryBody=$('#searchForm').find('#enterInputCategory');
	$brandBody.empty();
	$categoryBody.empty();

	var $brandReportBody=$('#inventory-report-form').find('#enterInputBrandReport');
    var $categoryReportBody=$('#inventory-report-form').find('#enterInputCategoryReport');
    $brandReportBody.empty();
    $categoryReportBody.empty();

	var brandSet =new Set();
	var categorySet=new Set();

	for(var i in data) {
		var e=data[i];
		brandSet.add(e.brand);
		categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	categorySet = Array.from(categorySet);
	categorySet.sort();

	var row='<option value="select">all brands</option>';
    $brandBody.append(row);
    $brandReportBody.append(row);
    row='<option value="select">all categories</option>';
    $categoryBody.append(row);
    $categoryReportBody.append(row);

	for(var i in brandSet) {
		row='<option value='+brandSet[i]+'>'+brandSet[i]+'</option>';
			$brandBody.append(row);
			$brandReportBody.append(row);
	}
	for(var i in categorySet) {
		row='<option value='+categorySet[i]+'>'+categorySet[i]+'</option>';
			$categoryBody.append(row);
			$categoryReportBody.append(row);
	}
}

function searchBrandCategory(event) {
    var barcode = $("#barcode-search").val();
    barcode = barcode.trim();

    var brand = $("#enterInputBrand :selected").text();
    var category = $("#enterInputCategory :selected").text();
    var obj = {barcode, brand, category, mrp: 0, name: ""};

    if(obj.brand === "all brands") {
        obj.brand = "";
    }

    if(obj.category === "all categories") {
            obj.category = "";
    }

    var json = JSON.stringify(obj);

    var url = getInventoryUrl() + "/filter";


        $.ajax({
           url: url,
           type: 'POST',
           data: json,
           headers: {
                    'Content-Type': 'application/json'
                  },
           success: function(data) {
                displayInventoryList(data);
           },
           error: handleAjaxError
        });

	return false;
}

function updateInventory(event) {
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	if(!isInt(JSON.parse(json).quantity)) {
        $.notify("Quantity must be Integer.")

        return;
    }

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-inventory-modal').modal('toggle');
	   		getInventoryList();
	   		$.notify("Inventory updated successfully.", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}


function getInventoryList() {
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteInventory(id) {
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();
	   },
	   error: handleAjaxError
	});
}

function downloadReport() {
    var $form = $("#inventory-report-form");
    var json = toJson($form);
    var obj = JSON.parse(json);
    obj = {barcode: obj.barcode, brand: obj.brand, category: obj.category, name: "", quantity: 0};

	if(obj.brand === "select") {
	    obj.brand = "";
	}

	if(obj.category === "select") {
    	    obj.category = "";
   	}

	var json = JSON.stringify(obj);

	var url = getInventoryUrl() + "/filter";
    $.ajax({
       url: url,
       type: 'POST',
       data: json,
       headers: {
                'Content-Type': 'application/json'
              },
       success: function(response) {

            if(response.length === 0) {
                $.notify("Brand and Category does not exist.");
                return;
            }

            var arr = [];

            for(var i = 0 ; i < response.length ; i++) {
                arr.push({barcode: response[i].barcode, brand: response[i].brand, category: response[i].category, name: response[i].name, quantity: response[i].quantity});
            }

            response = arr;

            var config = {
                                quoteChar: '',
                                escapeChar: '',
                                delimiter: "\t"
                            };

                            var data = Papa.unparse(response, config);
                            var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
                            var fileUrl =  null;
                            var currentdate = new Date();
                            var inventoryreportname = "inventory-report.tsv";
                            if (navigator.msSaveBlob) {
                                fileUrl = navigator.msSaveBlob(blob, brandreportname);
                            } else {
                                fileUrl = window.URL.createObjectURL(blob);
                            }
                            var tempLink = document.createElement('a');
                            tempLink.href = fileUrl;
                            tempLink.setAttribute('download', inventoryreportname);
                            tempLink.click();

                            $('#report-inventory-modal').modal('toggle');
                            $('#inventory-report-form').trigger("reset");
       },
       error: handleAjaxError
    });
}

var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	if(processCount == fileData.length){
		return;
	}

	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);

	if(!isInt(JSON.parse(json).quantity)) {
        row.error="Quantity must be Integer.";
        document.getElementById("download-errors").disabled = false;
        errorData.push(row);
       	uploadRows();

        return;
    }
	var url = getInventoryUrl() + '/search';

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {

	        response = response[0];

	        response.quantity = JSON.parse(json).quantity;

	        updateInventoryUpload(response.id, JSON.stringify(response), row);

	   		uploadRows();

	   },
	   error: function(response){
	   		row.error=JSON.parse(response.responseText).message;
	   		document.getElementById("download-errors").disabled = false;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function isInt(n) {
    return n % 1 === 0;
}

function updateInventoryUpload(id, json, row) {
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {

	   		getInventoryList();
	   },
	   error: function(response){
              	   		row.error=JSON.parse(response.responseText).message;
              	   		document.getElementById("download-errors").disabled = false;
              	   		errorData.push(row);
              	   		updateUploadDialog();
              	   }
	});
	return false;
}

function downloadErrors() {
	writeFileData(errorData);
}

function displayInventoryList(data) {

     data=data.sort(function(a,b){
		if(a.brand===b.brand){
			if(a.name<b.name){
				return -1;
			}else{
				return 1;
			}
		}else{
		if(a.brand<b.brand){
			return -1;
		}else{
			return 1;
		}
		}
	});

	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();

	for(var i in data) {

		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-info" onclick="displayEditInventory(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
        		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(id) {

	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog() {
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	processCount = 0;
	fileData = [];
	errorData = [];
	updateUploadDialog();
}

function updateUploadDialog() {
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName() {
	var $file = $('#inventoryFile');
	var path = $file.val();
    var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#inventoryFileName').html(fileName);
	document.getElementById("process-data").disabled = false;
}

function displayUploadData() {
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
	getInventoryList();
	document.getElementById("download-errors").disabled = true;
    document.getElementById("process-data").disabled = true;
}

function displayInventoryReportModal(){
	$('#report-inventory-modal').modal('toggle');
}

function cancelInventoryReportModal() {
    $('#report-inventory-modal').modal('toggle');
    $('#inventory-report-form').trigger("reset");
}

function displayInventory(data) {
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
    $("#inventory-edit-form input[name=brand").val(data.brand);
    $("#inventory-edit-form input[name=category").val(data.category);
    $("#inventory-edit-form input[name=name]").val(data.name);
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

function init() {
	$('#search-inventory').click(searchBrandCategory);
	$('#report-inventory').click(displayInventoryReportModal);
	$('#modal-cancel-inventory-report').click(cancelInventoryReportModal);
    $('#modal-cut-inventory-report').click(cancelInventoryReportModal);
    $('#modal-report-inventory').click(downloadReport);
	$('#update-inventory').click(updateInventory);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getInventoryList);
$(document).ready(getAllBrand);