
function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem";
}

function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function isInt(n) {
    return n % 1 === 0;
}

function addOrderItem(event) {

	var $form = $("#order-add-form");
	var json = toJson($form);
	var barcode = JSON.parse(json).barcode;
    barcode = barcode.trim();
    var quantity = JSON.parse(json).quantity;
    quantity = quantity.trim();
    var sellingPrice = JSON.parse(json).sellingPrice;
    sellingPrice = sellingPrice.trim();
    $form = $("#order-add-form-second");
    json = toJson($form);
    if(barcode === "") {
        $.notify("Barcode cannot be empty.")
        return;
    }
    if(quantity === "") {
        $.notify("Quantity cannot be empty.")
        return;
    }
    if(sellingPrice === "") {
        $.notify("Selling price cannot be empty.")
        return;
    }
    if(!isInt(quantity)) {
        $.notify("Quantity must be Integer.")
        return;
    }
    var num = Number(quantity);
    if(num <= 0) {
        $.notify("Quantity must be positive.")
        return;
    }
    if(num > Number(JSON.parse(json).availableQuantity)) {
        $.notify("Quantity cannot exceed available quantity limit.")
        return;
    }
    num = Number(sellingPrice);
    if(num <= 0) {
        $.notify("Selling Price must be positive.")
        return;
    }
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

                    if(data.length != 1) {
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
var updateData = [];

function checkAddTable(obj) {
    let flag = false;

    tableData.forEach(e => {
        if(e.barcode === obj.barcode) {
            flag = true;
        }
    });

    if(flag) {
        tableData = tableData.map(e => {

            if(e.barcode === obj.barcode) {
                return obj;
            }
            return e;
        });

        var table = document.getElementById('orderitem-table-add');

        for(var i = 1; i < table.rows.length; i++) {
              if(table.rows[i].cells[0].innerText === obj.barcode) {
                    table.rows[i].cells[3].innerText = obj.quantity;
                    table.rows[i].cells[4].innerText = commafy(parseFloat(obj.sellingPrice).toFixed(2));
               }
        }
        return true;
    }
    else {
        return false;
    }
}

function addOrderItemTable(obj) {
    if(checkAddTable(obj)) {
        $('#order-add-form').trigger("reset");
        $('#order-add-form-second').trigger("reset");
        return;
    }

    var $tbody = $('#orderitem-table-add').find('tbody');

    if(tableData.length === 0)
    {
        $tbody.empty();
    }

    tableData.push(obj);

    var buttonHtml= ' <button type="button" class="btn btn-danger" onclick="deleteAddOrderItemTable(\'' + obj.barcode + '\')">Remove</button>'
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
}

function checkEditTable(obj) {

    let flag = false;
    tableData.forEach(e => {
        if(e.barcode === obj.barcode) {
            flag = true;
        }
    });

    if(flag) {
        tableData = tableData.map(e => {

            if(e.barcode === obj.barcode) {
                return obj;
            }
            return e;
        });

        var table = document.getElementById('orderitem-table-edit');
        for(var i = 1; i < table.rows.length; i++) {
              if(table.rows[i].cells[0].innerText === obj.barcode) {
                    table.rows[i].cells[3].innerText = obj.quantity;
                    table.rows[i].cells[4].innerText = commafy(parseFloat(obj.sellingPrice).toFixed(2));;
               }
        }
        return true;
    }
    else {
        return false;
    }
}

function editOrderItemTable(obj, flag) {
    if(checkEditTable(obj)) {
        $('#order-edit-form').trigger("reset");
        $('#order-edit-form-second').trigger("reset");

        return;
    }

    tableData.push(obj);
    var $tbody = $('#orderitem-table-edit').find('tbody');

    var buttonHtml;
    buttonHtml = ' <button type="button" class="btn btn-danger" onclick="deleteEditOrderItemTable(\'' + obj.barcode + '\')">Remove</button>'

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
}

function viewOrderItemTable(obj) {
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




function deleteAddOrderItemTable(barcode) {

      var table = document.getElementById('orderitem-table-add');

      for(var i = 1; i < table.rows.length; i++) {
          if(table.rows[i].cells[0].innerText === barcode) {
              table.deleteRow(i);

              tableData = tableData.filter(e => {

                if(e.barcode === barcode) {
                    return false;
                }
                else {
                    return true;
                }
              });

              if(tableData.length === 0)
                {
                      console.log("hi");

                      var $tbody = $('#orderitem-table-add').find('tbody');

                      var row = '<tr style="background-color: white;">'
                      + '<td>' + 'No Data' + '</td>'
                      + '<td>' + '</td>'
                      + '<td>' + '</td>'
                      + '<td>' + '</td>'
                      + '<td>' + '</td>'
                      + '<td>' + '</td>'
                      + '</tr>';

                      $tbody.append(row);
                }

              return;
          }
      }
}

function deleteEditOrderItemTable(barcode) {

    var flag = false;
    updateData.forEach(e => {

        if(e === barcode) {
            flag = true;
        }
    })

    if(flag === true) {
        var obj;
        tableData.forEach(e => {

            if(e.barcode === barcode) {
                obj = e;
            }
        })
        obj.quantity = 0;
        checkEditTable(obj);
        return;
    }

      var table = document.getElementById('orderitem-table-edit');

      for(var i = 1; i < table.rows.length; i++) {

          if(table.rows[i].cells[0].innerText == barcode) {
              table.deleteRow(i);

              tableData = tableData.filter(e => {

                if(e.barcode === barcode) {
                    return false;
                }
                else {
                    return true;
                }
              });
              return;
          }
      }
}

function submitOrder() {

    var url = getOrderUrl();
    var json = JSON.stringify(tableData);

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
                    getOrderList();
                    $.notify("Order submitted successfully.", "success");
        	   },
         error: handleAjaxError
    });

	return false;
}


function editOrderItem(event) {

	var $form = $("#order-edit-form");
	var json = toJson($form);
	var barcode = JSON.parse(json).barcode;
    barcode = barcode.trim();
    var quantity = JSON.parse(json).quantity;
    quantity = quantity.trim();
    var sellingPrice = JSON.parse(json).sellingPrice;
    sellingPrice = sellingPrice.trim();
    $form = $("#order-edit-form-second");
    json = toJson($form);
    if(barcode === "") {
        $.notify("Barcode cannot be empty.")
        return;
    }
    if(quantity === "") {
        $.notify("Quantity cannot be empty.")
        return;
    }
    if(sellingPrice === "") {
        $.notify("Selling price cannot be empty.")
        return;
    }
    if(!isInt(quantity)) {
        $.notify("Quantity must be Integer.")
        return;
    }
    var num = Number(quantity);
    if(num <= 0) {
        $.notify("Quantity must be positive.")
        return;
    }
    if(num > Number(JSON.parse(json).availableQuantity)) {
        $.notify("Quantity cannot exceed available quantity limit.")
        return;
    }
    num = Number(sellingPrice);
    if(num <= 0) {
        $.notify("Selling Price must be positive.")
        return;
    }
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

                    if(data.length != 1) {
                        return;
                    }
                    data = data[0];
                    var obj = {barcode, brand: data.brand, name: data.name, quantity, sellingPrice };
        	   		editOrderItemTable(obj, false);
        	   },
         error: handleAjaxError
    });
	return false;
}

function updateOrder(event) {

    var id = $("#order-edit-form input[name=id]").val();
    var url = getOrderUrl() + "/" + id;
    var json = JSON.stringify(tableData);

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
                    updateData = [];
                    $('#edit-order-modal').modal('toggle');
                    $('#order-edit-form').trigger("reset");
                    $('#order-edit-form-second').trigger("reset");
                    getOrderList();
                    $.notify("Order updated successfully.", "success");
        	   },
         error: handleAjaxError
    });

	return false;
}

function getOrderList() {
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

function displayOrderList(data) {
    data = data.sort(function(a,b) {
        if(a.id > b.id) {
            return -1;
        }
        else {
        	return 1;
        }
    });

	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();

	if(data.length === 0)
    {
        var row = '<tr style="background-color: white;">'
        + '<td>' + 'No Data' + '</td>'
        + '<td>'  + '</td>'
        + '<td>' + '</td>'
        + '<td>' + '</td>'
        + '</tr>';
        $tbody.append(row);
    }

	for(var i in data){
		var e = data[i];
        var id = e.id;
        var st = id.toString();
        var orderId = "OD";
        for(var i = 0 ; i < 8 - st.length ; i++) {
            orderId += "0";
        }
        orderId += st;
		var buttonHtml;
		if(e.invoice === true) {
            buttonHtml = ' <button type="button" class="btn btn-info" disabled>Edit</button>'
        }
        else {
            buttonHtml = ' <button type="button" class="btn btn-info" onclick="displayEditOrder(' + e.id + ')">Edit</button>'
        }
		buttonHtml+= ' <button type="button" class="btn btn-info" onclick="displayViewOrder(' + e.id + ')">View</button>'
        buttonHtml+= ' <button type="button" class="btn btn-warning" onclick="generateInvoice(' + e.id + ')">Invoice</button>'

		var row = '<tr>'
		+ '<td>' + orderId + '</td>'
		+ '<td>'  + e.datetime + '</td>'
		+ '<td>'  + commafy(parseFloat(e.billAmount).toFixed(2)) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditOrder(id) {
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

function displayViewOrder(id) {
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

function searchByOrderId(event) {

	var od = $('#order-form').find('input[name="orderId"]').val();
    od = od.trim();
    if(od === "") {
        getOrderList();
        return;
    }
	if(!(od[0] === 'O' && od[1] === 'D')) {
        $.notify("Order Id doesn't exit.","error");
        return;
    }
    od = od.substring(2);
    if(!(od.length === 8)) {
        $.notify("Order Id doesn't exit.","error");
        return;
    }
    while(od.charAt(0) === '0') {
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

function displayOrder(id, data) {

     var $tbody = $('#orderitem-table-edit').find('tbody');
     $tbody.empty();
    data.forEach(d => {
        editOrderItemTable(d, true);
        updateData.push(d.barcode);
    });
    $("#order-edit-form input[name=id]").val(id);
	$('#edit-order-modal').modal('toggle');
}

function displayViewOrderModal(data) {

     var $tbody = $('#orderitem-table-view').find('tbody');
     $tbody.empty();
    data.forEach(d => {
        viewOrderItemTable(d);
    });
	$('#view-order-modal').modal('toggle');
}


function displayOrderModal() {
	$('#add-order-modal').modal('toggle');
	var $tbody = $('#orderitem-table-add').find('tbody');

    var row = '<tr style="background-color: white;">'
        + '<td>' + 'No Data' + '</td>'
        + '<td>' + '</td>'
        + '<td>' + '</td>'
        + '<td>' + '</td>'
        + '<td>' + '</td>'
        + '<td>' + '</td>'
        + '</tr>';

        $tbody.append(row);
}

function cancelOrderModal() {
    $('#add-order-modal').modal('toggle');
    $('#order-add-form').trigger("reset");
    $('#order-add-form-second').trigger("reset");
    tableData = [];
    var $tbody = $('#orderitem-table-add').find('tbody');
    $tbody.empty();
}

function cancelEditOrderModal() {
    $('#edit-order-modal').modal('toggle');
    $('#order-edit-form').trigger("reset");
    $('#order-edit-form-second').trigger("reset");
    tableData = [];
    updateData = [];
    var $tbody = $('#orderitem-table-edit').find('tbody');
    $tbody.empty();
}

function cancelViewOrderModal() {
    $('#view-order-modal').modal('toggle');
    tableData = [];
    var $tbody = $('#orderitem-table-view').find('tbody');
    $tbody.empty();
}

function getProductDetails(barcode) {
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

                    if(data.length != 1) {
                        $('#inputMrp').val("");
                        $('#inputBrand').val("");
                        $('#inputName').val("");
                        $('#inputQuantity').val("");
                        $('#inputAvailableQuantity').val("");
                        return;
                    }
                    data = data[0];
                    var obj1 = {barcode: data.barcode, brand: "", category: "", name: "", quantity: 0};
                    var json1 = JSON.stringify(obj1);
                    var url1 = getInventoryUrl() + "/filter";
                    $.ajax({
                       url: url1,
                       type: 'POST',
                       data: json1,
                       headers: {
                                'Content-Type': 'application/json'
                              },
                       success: function(data1) {

                            data1 = data1[0];
                            $('#inputMrp').val(data.mrp);
                            $('#inputBrand').val(data.brand);
                            $('#inputName').val(data.name);
                            $('#inputAvailableQuantity').val(data1.quantity);
                       },
                       error: handleAjaxError
                    });
        	   },
        	   error: function(error) {
                    $('#inputMrp').val("");
                    $('#inputBrand').val("");
                    $('#inputName').val("");
                    $('#inputQuantity').val("");
                    $('#inputAvailableQuantity').val("");
        	   }
    });
}

function getProductDetailsEdit(barcode) {
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

                    if(data.length != 1) {
                        $('#inputMrpEdit').val("");
                        $('#inputBrandEdit').val("");
                        $('#inputNameEdit').val("");
                        $('#inputQuantityEdit').val("");
                        $('#inputAvailableQuantityEdit').val("");
                        return;
                    }
                    data = data[0];
                    var obj1 = {barcode: data.barcode, brand: "", category: "", name: "", quantity: 0};
                    var json1 = JSON.stringify(obj1);
                    var url1 = getInventoryUrl() + "/filter";
                    $.ajax({
                       url: url1,
                       type: 'POST',
                       data: json1,
                       headers: {
                                'Content-Type': 'application/json'
                              },
                       success: function(data1) {

                            data1 = data1[0];
                            $('#inputMrpEdit').val(data.mrp);
                            $('#inputBrandEdit').val(data.brand);
                            $('#inputNameEdit').val(data.name);
                            $('#inputAvailableQuantityEdit').val(data1.quantity);
                       },
                       error: handleAjaxError
                    });
        	   },
        	   error: function(error) {
                    $('#inputMrpEdit').val("");
                    $('#inputBrandEdit').val("");
                    $('#inputNameEdit').val("");
                    $('#inputQuantityEdit').val("");
                    $('#inputAvailableQuantityEdit').val("");
        	   }
    });
}

function downloadBillPdf(id, blob) {

    var st = id.toString();
    var orderId = "OD";
    for(var i = 0 ; i < 8 - st.length ; i++) {
        orderId += "0";
    }
    orderId += st;
	let link = document.createElement('a');
	link.href = window.URL.createObjectURL(blob);
	var currentdate = new Date();
	link.download = "invoice-" + orderId +".pdf";
	link.click();
}


function generateInvoice(id) {

    var url = getOrderItemUrl() + "/" + id;
    $.ajax({
       url: url,
       type: 'GET',
       success: function(dataRec) {
        var url=getOrderUrl() + "/invoice/" + id;
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(dataRec),
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            success: function(data) {

            let binaryString = window.atob(data);
            let binaryLen = binaryString.length;
            let bytes = new Uint8Array(binaryLen);

            for (let i = 0; i < binaryLen; i++) {
                let ascii = binaryString.charCodeAt(i);
                bytes[i] = ascii;
            }
            let blob = new Blob([bytes], {type: "application/pdf"});
            downloadBillPdf(id, blob);
            getOrderList();
       },
       error: handleAjaxError
    });
    },
    error: handleAjaxError
});
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

	var $brandReportBody=$('#sales-report-form').find('#enterInputBrandReport');
    var $categoryReportBody=$('#sales-report-form').find('#enterInputCategoryReport');
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

	var row='<option value="select">select brand</option>';
    $brandReportBody.append(row);
    row='<option value="select">select category</option>';
    $categoryReportBody.append(row);

	for(var i in brandSet) {
		row='<option value='+brandSet[i]+'>'+brandSet[i]+'</option>';
			$brandReportBody.append(row);
	}
	for(var i in categorySet) {
		row='<option value='+categorySet[i]+'>'+categorySet[i]+'</option>';
			$categoryReportBody.append(row);
	}
}

function downloadReport() {
    var $form = $("#sales-report-form");
    var json = toJson($form);
    var obj = JSON.parse(json);
    if(obj.startDate == "") {
        obj.startDate = "1950-01-01";
    }
    if(obj.endDate == "") {
        obj.endDate = "2150-01-01";
    }
	if(obj.brand === "select") {
	    obj.brand = "";
	}
	if(obj.category === "select") {
    	    obj.category = "";
   	}
	var json = JSON.stringify(obj);


	var url = getOrderUrl() + "/sales";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
                  	'Content-Type': 'application/json'
                  },
    	   success: function(response) {

    	        if(response.length === 0) {
    	            $.notify("No Order exists for this filter.");
    	            return;
    	        }

    	        for(var i = 0 ; i < response.length ; i++) {
                    response[i].revenue = parseFloat(response[i].revenue).toFixed(2);
                }

                response=response.sort(function(a,b) {
                    if(a.brand === b.brand) {

                        if(a.category<b.category) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                    if(a.brand < b.brand) {
                        return -1;
                    } else {
                        return 1;
                    }
                    }
                });

    	   		var config = {
                            		quoteChar: '',
                            		escapeChar: '',
                            		delimiter: "\t"
                            	};

                            	var data = Papa.unparse(response, config);
                                var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
                                var fileUrl =  null;
                                var currentdate = new Date();
                                var inventoryreportname = "sales-report.tsv";
                                if (navigator.msSaveBlob) {
                                    fileUrl = navigator.msSaveBlob(blob, brandreportname);
                                } else {
                                    fileUrl = window.URL.createObjectURL(blob);
                                }
                                var tempLink = document.createElement('a');
                                tempLink.href = fileUrl;
                                tempLink.setAttribute('download', inventoryreportname);
                                tempLink.click();

                                $('#report-sales-modal').modal('toggle');
                                $('#sales-report-form').trigger("reset");
    	   },
    	   error: handleAjaxError
    	});
}

function displaySalesReportModal(){
	$('#report-sales-modal').modal('toggle');
}

function cancelSalesReportModal()
{
    $('#report-sales-modal').modal('toggle');
    $('#sales-report-form').trigger("reset");
}

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
    $('#report-sales').click(displaySalesReportModal);
    $('#modal-report-sales').click(downloadReport);
    $('#modal-cancel-sales-report').click(cancelSalesReportModal);
    $('#modal-cut-sales-report').click(cancelSalesReportModal);
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
$(document).ready(getAllBrand);