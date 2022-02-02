
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){

	//Set the values to update
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
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){

	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
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
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
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

function searchBrandList(){

    var $form = $("#brand-form");
    var json = toJson($form);
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

function deleteBrand(id){
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

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);

}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   		getBrandList();
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		document.getElementById("download-errors").disabled = false;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml= /*'<button type="button" class="btn btn-info" onclick="deleteBrand(' + e.id + ')">delete</button>'
            buttonHtml +=*/ ' <button type="button" class="btn btn-info" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
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

function brandFilter() {

    var value = document.getElementById("brand-filter").value;
    value = value.trim();
    value = value.toLowerCase();

    if(value === '')
    {
        getBrandList();

        return;
    }

    $("#brand-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
    });
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
	document.getElementById("process-data").disabled = false;
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
	getBrandList();
	document.getElementById("download-errors").disabled = true;
	document.getElementById("process-data").disabled = true;
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}


function displayBrandModal(){
	$('#add-brand-modal').modal('toggle');
}

function cancelBrandModal()
{
    $('#add-brand-modal').modal('toggle');
    $('#brand-add-form').trigger("reset");
}

//INITIALIZATION CODE
function init(){
	$('#add-brand').click(displayBrandModal);
	$('#search-brand').click(brandFilter);
	$('#modal-add-brand').click(addBrand);
	$('#modal-cancel-brand').click(cancelBrandModal);
	$('#modal-cut-brand').click(cancelBrandModal);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);

    document.getElementById('brand-filter').addEventListener('submit', function(e) {
        e.preventDefault();
    });
}

$(document).ready(init);
$(document).ready(getBrandList);

