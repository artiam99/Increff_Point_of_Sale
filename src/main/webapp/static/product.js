
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-add-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {

            $('#add-product-modal').modal('toggle');
	        $('#product-add-form').trigger("reset");
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){

	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#edit-product-modal').modal('toggle');
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
}

function searchProductList(){

    var barcode = $('#product-form').find('input[name="barcode"]').val();
    barcode = barcode.trim();

    if(barcode === '')
    {
        getProductList();

        return;
    }

	var url = getProductUrl() + '/search';

	var json = JSON.stringify({ message: barcode});

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
       	   headers: {
                     	'Content-Type': 'application/json'
                     },
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
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
	var url = getProductUrl();

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
	   		getProductList();
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		document.getElementById("download-errors").disabled = false;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function productFilter() {

    var value = document.getElementById("product-filter").value;
    value = value.trim();
    value = value.toLowerCase();


    if(value === '')
    {
        getProductList();

        return;
    }

    $("#product-table-body tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
    });
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn btn-info" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
        		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
	document.getElementById("process-data").disabled = false;
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
	getProductList();
	document.getElementById("download-errors").disabled = true;
    document.getElementById("process-data").disabled = true;
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
    $("#product-edit-form input[name=brand").val(data.brand);
    $("#product-edit-form input[name=category").val(data.category);
    $("#product-edit-form input[name=name]").val(data.name);
    $("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}

function displayProductModal(){
	$('#add-product-modal').modal('toggle');
}

function cancelProductModal()
{
    $('#add-product-modal').modal('toggle');
    $('#product-add-form').trigger("reset");
}



//INITIALIZATION CODE
function init(){
	$('#add-product').click(displayProductModal);
	$('#search-product').click(productFilter);
	$('#modal-add-product').click(addProduct);
	$('#modal-cancel-product').click(cancelProductModal);
	$('#modal-cut-product').click(cancelProductModal);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);
