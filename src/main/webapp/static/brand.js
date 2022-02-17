
function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function addBrand(event) {
	var $form = $("#brand-add-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {

            $('#add-brand-modal').modal('toggle');
	        $('#brand-add-form').trigger("reset");
	   		getBrandList();
	   		getAllBrand();
	   		$.notify("Brand and Category added successfully.", "success");
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateBrand(event) {
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-brand-modal').modal('toggle');
	   		getBrandList();
	   		getAllBrand();
	   		$.notify("Brand and Category updated successfully.", "success");
	   },
	   error: handleAjaxError
	});
	return false;
}

function getBrandList() {
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteBrand(id) {
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandList();
	   },
	   error: handleAjaxError
	});
}

var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);

}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	updateUploadDialog();
	if(processCount == fileData.length){
	    getBrandList();
	    getAllBrand();
		return;
	}

	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
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

function downloadErrors(){
	writeFileData(errorData);
}

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();

	if(data.length === 0)
	{
	    var row = '<tr style="background-color: white;">'
        + '<td>' + 'No Data' + '</td>'
        + '<td>'  + '</td>'
        + '<td>' + '</td>'
        + '</tr>';
        $tbody.append(row);
	}

	for(var i in data){
		var e = data[i];
		var buttonHtml= '<button type="button" class="btn btn-info" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function getAllBrand(){
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
function displaySearchBrandCategory(data){
	var $brandBody=$('#searchForm').find('#enterInputBrand');
	var $categoryBody=$('#searchForm').find('#enterInputCategory');
	$brandBody.empty();
	$categoryBody.empty();

	var $brandReportBody=$('#brand-report-form').find('#enterInputBrandReport');
    var $categoryReportBody=$('#brand-report-form').find('#enterInputCategoryReport');
    $brandReportBody.empty();
    $categoryReportBody.empty();

	var brandSet = new Set();
	var categorySet = new Set();

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

	for(var i in brandSet){
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


function searchBrandCategory(){
    var brand = $("#enterInputBrand :selected").text();
    var category = $("#enterInputCategory :selected").text();
	var obj = {brand, category};

	if(obj.brand === "all brands") {
	    obj.brand = "";
	}

	if(obj.category === "all categories") {
    	    obj.category = "";
   	}

	var json = JSON.stringify(obj);

	var url = getBrandUrl() + "/search";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
                  	'Content-Type': 'application/json'
                  },
    	   success: function(data) {
    	   		displayBrandList(data);
    	   },
    	   error: handleAjaxError
    	});
}

function downloadReport(){
    var brand = $("#enterInputBrandReport :selected").text();
    var category = $("#enterInputCategoryReport :selected").text();
	var obj = {brand, category};

	if(obj.brand === "all brands") {
	    obj.brand = "";
	}

	if(obj.category === "all categories") {
    	    obj.category = "";
   	}

	var json = JSON.stringify(obj);

	var url = getBrandUrl() + "/search";
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
    	            arr.push({brand: response[i].brand, category: response[i].category});
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
                                var brandreportname = "brand-report.tsv";
                                if (navigator.msSaveBlob) {
                                    fileUrl = navigator.msSaveBlob(blob, brandreportname);
                                } else {
                                    fileUrl = window.URL.createObjectURL(blob);
                                }
                                var tempLink = document.createElement('a');
                                tempLink.href = fileUrl;
                                tempLink.setAttribute('download', brandreportname);
                                tempLink.click();

                                $('#report-brand-modal').modal('toggle');
                                $('#brand-report-form').trigger("reset");
    	   },
    	   error: handleAjaxError
    	});
}


function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog() {
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var $file = $('#brandFile');
	var path = $file.val();
    var fileName = path.replace(/^C:\\fakepath\\/, "");
	$('#brandFileName').html(fileName);
	document.getElementById("process-data").disabled = false;
}

function displayUploadData() {
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
	getBrandList();
	document.getElementById("download-errors").disabled = true;
	document.getElementById("process-data").disabled = true;
}

function displayBrand(data) {
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

function displayBrandModal(){
	$('#add-brand-modal').modal('toggle');
}

function displayBrandReportModal() {
	$('#report-brand-modal').modal('toggle');
}

function cancelBrandModal() {
    $('#add-brand-modal').modal('toggle');
    $('#brand-add-form').trigger("reset");
}

function cancelBrandReportModal() {
    $('#report-brand-modal').modal('toggle');
    $('#brand-report-form').trigger("reset");
}

function init() {
	$('#add-brand').click(displayBrandModal);
	$('#report-brand').click(displayBrandReportModal);
	$('#modal-add-brand').click(addBrand);
	$('#modal-report-brand').click(downloadReport);
	$('#modal-cancel-brand').click(cancelBrandModal);
	$('#modal-cut-brand').click(cancelBrandModal);
	$('#modal-cancel-brand-report').click(cancelBrandReportModal);
    $('#modal-cut-brand-report').click(cancelBrandReportModal);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    $('#search-brand').click(searchBrandCategory);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getAllBrand);