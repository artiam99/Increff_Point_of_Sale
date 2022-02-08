
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-add-form");
	var json = toJson($form);
    var obj = JSON.parse(json);

    if(obj.brand === "select")
    {
   	    obj.brand = "";
   	}

   	if(obj.category === "select")
    {
       	obj.category = "";
    }

    json = JSON.stringify(obj);

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
	   		$.notify("Product added successfully.", "success");
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
    var obj = JSON.parse(json);

    obj.barcode = obj.barcode.trim();
    obj.name = obj.name.trim();

    if(obj.barcode === "")
    {
        $.notify("Barcode cannot be empty.", "error");

        return;
    }

    if(obj.name === "")
    {
        $.notify("Name cannot be empty.", "error");

        return;
    }


    if(obj.brand === "select")
    {
        obj.brand = "";
    }

    if(obj.category === "select")
    {
        obj.category = "";
    }

    json = JSON.stringify(obj);

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
	   		$.notify("Product updated successfully.", "success");
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

function commafy( numb ) {
var str = numb.toString().split(".");
    str[0] = str[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return str.join(".");
}

//UI DISPLAY METHODS
function displayProductList(data){
    data=data.sort(function(a,b){
    		if(a.brand===b.brand){
    			if(a.category<b.category){
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
        		+ '<td>'  + commafy(parseFloat(e.mrp).toFixed(2)) + '</td>'
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
	var path = $file.val();
    var fileName = path.replace(/^C:\\fakepath\\/, "");
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

function getAllBrand(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	        displaySearchBrandCategory(data);
	   		displaySearchBrandCategoryAdd(data);
	   		displaySearchBrandCategoryEdit(data);
	   },
	   error: handleAjaxError
	});
}

function displaySearchBrandCategory(data){
	var $brandBody=$('#searchForm').find('#enterInputBrand');
	var $categoryBody=$('#searchForm').find('#enterInputCategory');
	$brandBody.empty();
	$categoryBody.empty();
	var brandSet =new Set();
	var categorySet=new Set();

	for(var i in data){
		var e=data[i];
		brandSet.add(e.brand);
		categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	categorySet = Array.from(categorySet);
	categorySet.sort();

	var row='<option value="select">select brand</option>';
    $brandBody.append(row);
    row='<option value="select">select category</option>';
    $categoryBody.append(row);

	for(var i in brandSet){
		row='<option value='+brandSet[i]+'>'+brandSet[i]+'</option>';
			$brandBody.append(row);
	}
	for(var i in categorySet){

		row='<option value='+categorySet[i]+'>'+categorySet[i]+'</option>';
			$categoryBody.append(row);
	}
}


function displaySearchBrandCategoryAdd(data){
	var $brandBody=$('#product-add-form').find('#enterInputBrandAdd');
	var $categoryBody=$('#product-add-form').find('#enterInputCategoryAdd');
	$brandBody.empty();
	$categoryBody.empty();
	var brandSet =new Set();
	var categorySet=new Set();

	for(var i in data){
		var e=data[i];
		brandSet.add(e.brand);
		categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	categorySet = Array.from(categorySet);
    categorySet.sort();


	var row='<option value="select">select brand</option>';
    $brandBody.append(row);
    row='<option value="select">select category</option>';
    $categoryBody.append(row);

	for(var i in brandSet){
		row='<option value='+brandSet[i]+'>'+brandSet[i]+'</option>';
			$brandBody.append(row);
	}
	for(var i in categorySet){

		row='<option value='+categorySet[i]+'>'+categorySet[i]+'</option>';
			$categoryBody.append(row);
	}
}

function displaySearchBrandCategoryEdit(data){
	var $brandBody=$('#product-edit-form').find('#enterInputBrandEdit');
	var $categoryBody=$('#product-edit-form').find('#enterInputCategoryEdit');
	$brandBody.empty();
	$categoryBody.empty();
	var brandSet =new Set();
	var categorySet=new Set();

	for(var i in data){
		var e=data[i];
		brandSet.add(e.brand);
		categorySet.add(e.category);
	}

	brandSet = Array.from(brandSet);
	categorySet = Array.from(categorySet);
	categorySet.sort();

	var row='';

	for(var i in brandSet){
		row='<option value='+brandSet[i]+'>'+brandSet[i]+'</option>';
			$brandBody.append(row);
	}
	for(var i in categorySet){

		row='<option value='+categorySet[i]+'>'+categorySet[i]+'</option>';
			$categoryBody.append(row);
	}
}

function searchBrandCategory(){

    var barcode = $("#barcode-search").val();
    barcode = barcode.trim();

    var brand = $("#enterInputBrand :selected").text();
    var category = $("#enterInputCategory :selected").text();
	var obj = {barcode, brand, category, mrp: 0, name: ""};

	if(obj.brand === "select brand")
	{
	    obj.brand = "";
	}

	if(obj.category === "select category")
    {
    	    obj.category = "";
   	}

	var json = JSON.stringify(obj);

	var url = getProductUrl() + "/search";


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

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
    $('#enterInputBrandEdit').val(data.brand)
    $('#enterInputCategoryEdit').val(data.category);
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
	$('#search-product').click(searchBrandCategory);
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
$(document).ready(getAllBrand);
