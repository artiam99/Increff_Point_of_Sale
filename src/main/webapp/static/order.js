
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addOrderItem(event){

	//Set the values to update
	var $form = $("#order-add-form");
	var json = toJson($form);
	var barcode = JSON.parse(json).barcode;
    barcode = barcode.trim();
    var quantity = JSON.parse(json).quantity;
    var sellingPrice = JSON.parse(json).sellingPrice;
    var obj = {barcode, brand: "", category:"", mrp: 0, name: ""};

    json = JSON.stringify(obj);

    var url = getProductUrl() + "/search";

    $.ajax({
         url: url,
         type: 'POST',
         data: json,
         headers: {
             'Content-Type': 'application/json'
                   },
         success: function(data) {

                    if(data.length != 1)
                    {
                        return;
                    }

                    data = data[0];


                    var obj = {barcode, brand: data.brand, name: data.name, quantity, sellingPrice };

        	   		addOrderItemTable(obj);
        	   },
         error: handleAjaxError
    });

	return false;
}

var tableData = [];

function checkAddTable(obj)
{
    let flag = false;

    tableData.forEach(e =>
    {
        if(e.barcode === obj.barcode)
        {
            flag = true;
        }
    });

    if(flag)
    {
        tableData = tableData.map(e =>
        {
            if(e.barcode === obj.barcode)
            {
                return obj;
            }

            return e;
        });

        var table = document.getElementById('orderitem-table-add');
        for(var i = 1; i < table.rows.length; i++)
        {
              if(table.rows[i].cells[0].innerText === obj.barcode)
               {
                    table.rows[i].cells[3].innerText = obj.quantity;
                    table.rows[i].cells[4].innerText = obj.sellingPrice;
               }
        }

        return true;
    }
    else
    {
        return false;
    }
}

function addOrderItemTable(obj)
{
    if(checkAddTable(obj))
    {
        $('#order-add-form').trigger("reset");
        $('#order-add-form-second').trigger("reset");
        $("#order-add-form input[name=quantity]").val(1);

        return;
    }

    tableData.push(obj);

    var $tbody = $('#orderitem-table-add').find('tbody');

    var buttonHtml= ' <button type="button" class="btn btn-danger" onclick="deleteAddOrderItemTable(\'' + obj.barcode + '\')">Delete</button>'
    var row = '<tr>'
        + '<td>' + obj.barcode + '</td>'
        + '<td>'  + obj.brand + '</td>'
        + '<td>'  + obj.name + '</td>'
        + '<td>'  + obj.quantity + '</td>'
        + '<td>'  + commafy(parseFloat(obj.sellingPrice).toFixed(2)) + '</td>'
        + '<td>' + buttonHtml + '</td>'
        + '</tr>';

        $tbody.append(row);

        $('#order-add-form').trigger("reset");
        $('#order-add-form-second').trigger("reset");
        $("#order-add-form input[name=quantity]").val(1);
}

function checkEditTable(obj)
{
    let flag = false;

    tableData.forEach(e =>
    {
        if(e.barcode === obj.barcode)
        {
            flag = true;
        }
    });

    if(flag)
    {
        tableData = tableData.map(e =>
        {
            if(e.barcode === obj.barcode)
            {
                return obj;
            }

            return e;
        });

        var table = document.getElementById('orderitem-table-edit');
        for(var i = 1; i < table.rows.length; i++)
        {
              if(table.rows[i].cells[0].innerText === obj.barcode)
               {
                    table.rows[i].cells[3].innerText = obj.quantity;
                    table.rows[i].cells[4].innerText = obj.sellingPrice;
               }
        }

        return true;
    }
    else
    {
        return false;
    }
}

function editOrderItemTable(obj)
{
    if(checkEditTable(obj))
    {
        $('#order-edit-form').trigger("reset");
        $('#order-edit-form-second').trigger("reset");
        $("#order-edit-form input[name=quantity]").val(1);

        return;
    }

    tableData.push(obj);

    var $tbody = $('#orderitem-table-edit').find('tbody');

    var buttonHtml= ' <button type="button" class="btn btn-danger" onclick="deleteEditOrderItemTable(\'' + obj.barcode + '\')">Delete</button>'
    var row = '<tr>'
        + '<td>' + obj.barcode + '</td>'
        + '<td>'  + obj.brand + '</td>'
        + '<td>'  + obj.name + '</td>'
        + '<td>'  + obj.quantity + '</td>'
        + '<td>'  + commafy(parseFloat(obj.sellingPrice).toFixed(2)) + '</td>'
        + '<td>' + buttonHtml + '</td>'
        + '</tr>';

        $tbody.append(row);

        $('#order-edit-form').trigger("reset");
        $('#order-edit-form-second').trigger("reset");
        $("#order-edit-form input[name=quantity]").val(1);
}

function viewOrderItemTable(obj)
{
    tableData.push(obj);

    var $tbody = $('#orderitem-table-view').find('tbody');

    var row = '<tr>'
        + '<td>' + obj.barcode + '</td>'
        + '<td>'  + obj.brand + '</td>'
        + '<td>'  + obj.name + '</td>'
        + '<td>'  + obj.quantity + '</td>'
        + '<td>'  + commafy(parseFloat(obj.sellingPrice).toFixed(2)) + '</td>'
        + '</tr>';

        $tbody.append(row);
}




function deleteAddOrderItemTable(barcode)
{
      var table = document.getElementById('orderitem-table-add');

      for(var i = 1; i < table.rows.length; i++)
      {
          if(table.rows[i].cells[0].innerText === barcode)
          {
              table.deleteRow(i);

              tableData = tableData.filter(e =>
              {
                if(e.barcode === barcode)
                {
                    return false;
                }
                else
                {
                    return true;
                }
              });

              return;
          }
      }
}

function deleteEditOrderItemTable(barcode)
{
      var table = document.getElementById('orderitem-table-edit');

      for(var i = 1; i < table.rows.length; i++)
      {
          if(table.rows[i].cells[0].innerText == barcode)
          {
              table.deleteRow(i);

              tableData = tableData.filter(e =>
              {
                if(e.barcode === barcode)
                {
                    return false;
                }
                else
                {
                    return true;
                }
              });

              return;
          }
      }
}

function submitOrder(){

    var url = getOrderUrl();

    var json = JSON.stringify(tableData);

    console.log(json);


    $.ajax({
         url: url,
         type: 'POST',
         data: json,
         headers: {
             'Content-Type': 'application/json'
                   },
         success: function(response) {

                    var $tbody = $('#orderitem-table-add').find('tbody');
                    $tbody.empty();

                    tableData = [];

                    $('#add-order-modal').modal('toggle');
                    $('#order-add-form').trigger("reset");
                    $('#order-add-form-second').trigger("reset");
                    $("#order-add-form input[name=quantity]").val(1);

                    getOrderList();
        	   },
         error: handleAjaxError
    });

	return false;
}


function editOrderItem(event){

	//Set the values to update
	var $form = $("#order-edit-form");
	var json = toJson($form);
	var barcode = JSON.parse(json).barcode;
    barcode = barcode.trim();
    var quantity = JSON.parse(json).quantity;
    var sellingPrice = JSON.parse(json).sellingPrice;
    var obj = {barcode, brand: "", category:"", mrp: 0, name: ""};

    json = JSON.stringify(obj);

    var url = getProductUrl() + "/search";

    $.ajax({
         url: url,
         type: 'POST',
         data: json,
         headers: {
             'Content-Type': 'application/json'
                   },
         success: function(data) {

                    if(data.length != 1)
                    {
                        return;
                    }

                    data = data[0];

                    var obj = {barcode, brand: data.brand, name: data.name, quantity, sellingPrice };

        	   		editOrderItemTable(obj);
        	   },
         error: handleAjaxError
    });

	return false;
}

function updateOrder(event){

    var id = $("#order-edit-form input[name=id]").val();
    var url = getOrderUrl() + "/" + id;

    var json = JSON.stringify(tableData);

    console.log(json);


    $.ajax({
         url: url,
         type: 'PUT',
         data: json,
         headers: {
             'Content-Type': 'application/json'
                   },
         success: function(response) {

                    var $tbody = $('#orderitem-table-edit').find('tbody');
                    $tbody.empty();

                    tableData = [];

                    $('#edit-order-modal').modal('toggle');
                    $('#order-edit-form').trigger("reset");
                    $('#order-edit-form-second').trigger("reset");
                    $("#order-edit-form input[name=quantity]").val(1);

                    getOrderList();
        	   },
         error: handleAjaxError
    });

	return false;
}


function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function commafy( numb ) {
var str = numb.toString().split(".");
    str[0] = str[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return str.join(".");
}

function displayOrderList(data){
    data=data.sort(function(a,b){
        		if(a.id>b.id){
        			return -1;
        		}
        		else{
        			return 1;
        		}
        	});

	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
        var id = e.id;
        var st = id.toString();

        var orderId = "OD";

        for(var i = 0 ; i < 8 - st.length ; i++)
        {
            orderId += "0";
        }

        orderId += st;
		var buttonHtml= ' <button type="button" class="btn btn-info" onclick="displayEditOrder(' + e.id + ')">Edit</button>'
		buttonHtml+= ' <button type="button" class="btn btn-info" onclick="displayViewOrder(' + e.id + ')">View</button>'
		var row = '<tr>'
		+ '<td>' + orderId + '</td>'
		+ '<td>'  + e.datetime + '</td>'
		+ '<td>'  + commafy(parseFloat(e.billAmount).toFixed(2)) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditOrder(id){
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(id, data);
	   },
	   error: handleAjaxError
	});
}

function displayViewOrder(id){
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayViewOrderModal(data);
	   },
	   error: handleAjaxError
	});
}

function searchByOrderId(event){

	var od = $('#order-form').find('input[name="orderId"]').val();
    od = od.trim();

    if(od === "")
    {
        getOrderList();

        return;
    }

	if(!(od[0] === 'O' && od[1] === 'D'))
    {
        $.notify("Order Id doesn't exit.","error");

        return;
    }
    od= od.substring(2);
    if(!(od.length === 8))
    {
        $.notify("Order Id doesn't exit.","error");

        return;
    }

    while(od.charAt(0) === '0')
    {
        od = od.substring(1);
    }

    const num = Number(od);

    if(!(Number.isInteger(num))) {

        $.notify("Order Id doesn't exit.","error");
        return;
    }

	var url = getOrderUrl() + "/" + od ;

	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {

    	        var arr = [];

    	        arr.push(data);

    	   		displayOrderList(arr);
    	   },
    	   error: function()
    	   {
    	        $.notify("Order Id doesn't exit.","error");
    	   }
    	});

	return false;
}

function displayOrder(id, data){

     var $tbody = $('#orderitem-table-edit').find('tbody');
     $tbody.empty();

    data.forEach(d =>
    {
        editOrderItemTable(d);
    });


    $("#order-edit-form input[name=quantity]").val(1);
    $("#order-edit-form input[name=id]").val(id);
	$('#edit-order-modal').modal('toggle');
}

function displayViewOrderModal(data){

     var $tbody = $('#orderitem-table-view').find('tbody');
     $tbody.empty();

    data.forEach(d =>
    {
        viewOrderItemTable(d);
    });

	$('#view-order-modal').modal('toggle');
}


function displayOrderModal(){
    $("#order-add-form input[name=quantity]").val(1);
	$('#add-order-modal').modal('toggle');
}

function cancelOrderModal()
{
    $('#add-order-modal').modal('toggle');
    $('#order-add-form').trigger("reset");
    $('#order-add-form-second').trigger("reset");
    $("#order-add-form input[name=quantity]").val(1);
    tableData = [];
    var $tbody = $('#orderitem-table-add').find('tbody');
    $tbody.empty();
}

function cancelEditOrderModal()
{
    $('#edit-order-modal').modal('toggle');
    $('#order-edit-form').trigger("reset");
    $('#order-edit-form-second').trigger("reset");
    $("#order-edit-form input[name=quantity]").val(1);
    tableData = [];
    var $tbody = $('#orderitem-table-edit').find('tbody');
    $tbody.empty();
}

function cancelViewOrderModal()
{
    $('#view-order-modal').modal('toggle');
    tableData = [];
    var $tbody = $('#orderitem-table-view').find('tbody');
    $tbody.empty();
}

function getProductDetails(barcode){
    barcode = barcode.trim();

	var obj = {barcode, brand: "", category: "", mrp: 0, name: ""};

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

                    if(data.length != 1)
                    {
                        $('#inputMrp').val("");
                        $('#inputBrand').val("");
                        $('#inputName').val("");

                        return;
                    }

                    data = data[0];

        	   		$('#inputMrp').val(data.mrp);
        	   		$('#inputBrand').val(data.brand);
                    $('#inputName').val(data.name);
        	   },
        	   error: function(error)
        	   {
                    $('#inputMrp').val("");
                    $('#inputBrand').val("");
                    $('#inputName').val("");
        	   }
    });
}

function getProductDetailsEdit(barcode){
    barcode = barcode.trim();

	var obj = {barcode, brand: "", category: "", mrp: 0, name: ""};

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

                    if(data.length != 1)
                    {
                        $('#inputMrpEdit').val("");
                        $('#inputBrandEdit').val("");
                        $('#inputNameEdit').val("");

                        return;
                    }

                    data = data[0];

        	   		$('#inputMrpEdit').val(data.mrp);
        	   		$('#inputBrandEdit').val(data.brand);
                    $('#inputNameEdit').val(data.name);
        	   },
        	   error: function(error)
        	   {
                    $('#inputMrpEdit').val("");
                    $('#inputBrandEdit').val("");
                    $('#inputNameEdit').val("");
        	   }
    });
}

//INITIALIZATION CODE
function init(){
    $('#search-order').click(searchByOrderId);
	$('#add-order').click(displayOrderModal);
	$('#submit-order').click(submitOrder);
	$('#modal-add-orderItem').click(addOrderItem);
	$('#modal-edit-orderItem').click(editOrderItem);
	$('#modal-cancel-order').click(cancelOrderModal);
	$('#modal-cut-order').click(cancelOrderModal);
	$('#modal-cancel-edit-order').click(cancelEditOrderModal);
    $('#modal-cut-edit-order').click(cancelEditOrderModal);
    $('#modal-cut-view-order').click(cancelViewOrderModal);
	$('#edit-order').click(updateOrder);
	$("#inputBarcode").on('input',function(){
    	      	getProductDetails($(this).val());
    	  });
    $("#inputBarcodeEdit").on('input',function(){
        	      	getProductDetailsEdit($(this).val());
        	  });
}

$(document).ready(init);
$(document).ready(getOrderList);