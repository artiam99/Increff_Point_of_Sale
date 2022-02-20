# Increff PoSify

A PoS (Point of Sale) system is what we see in grocery stores, where the sales manager scans our products and gives us a receipt.

PoSify is a PoS (Point of Sale) system where you can add brands & products, keep track of inventory and create orders and their reports.

## Functionalities

1.	Upload brand/category details (Brand Master) using TSV, from UI
2.	View + Create + Edit a brand detail using UI
3.	Upload product details using TSV, from UI
4.	View + Create + Edit a product detail using UI
5.	Upload product wise inventory using TSV
6.	Edit inventory for a product
7.	Create a customer order -
    1.	Enter barcode, Quantity, MRP
    2.	On creating order, the inventory is reduced
8.	Edit an existing customer order
9.	Download a PDF for a customer invoice
10.	Reports -
    1.	Inventory report 
    2.	Brand
    3.	reportSales report (for a duration)


## Commands

Build: __mvn clean install__

Run: __mvn jetty:run__

## Home Page

Home Page gives option for moving to any of the 4 major pages.

![Screenshot (8)](https://user-images.githubusercontent.com/47227715/154834941-f5a8fba5-34e8-4249-bfe8-faf6cd2d9fbc.png)

## Brand Page

Brand Page gives option for creating and editing brands, searching brands by brand or category, uploading one or more brands together from TSV file and downloading Brand Report in TSV format.

![Screenshot (9)](https://user-images.githubusercontent.com/47227715/154835059-b02aaa59-072d-4106-bf97-af033bbe2ab2.png)
![Screenshot (29)](https://user-images.githubusercontent.com/47227715/154835253-4f82dd36-20cd-47ea-bfaa-626cfd307c18.png)
![Screenshot (13)](https://user-images.githubusercontent.com/47227715/154835159-81acf85f-e63e-42d2-911b-a3c572be4d97.png)
![Screenshot (11)](https://user-images.githubusercontent.com/47227715/154835168-03b17b2d-dbfc-48f3-a90e-953f53422935.png)
![Screenshot (12)](https://user-images.githubusercontent.com/47227715/154835174-de27175b-4fb1-46c8-be1e-098df2f2047c.png)

## Product Page

Product Page gives option for creating and editing products, searching products by brand, category or barcode, uploading one or more prodducts together from TSV file.

![Screenshot (14)](https://user-images.githubusercontent.com/47227715/154835524-4952c1e5-05c2-44f8-b52c-3d1abe4908bb.png)
![Screenshot (15)](https://user-images.githubusercontent.com/47227715/154835530-291f18ba-6c55-4d25-ab49-954129c25b21.png)
![Screenshot (17)](https://user-images.githubusercontent.com/47227715/154835533-0461649f-7b07-40ad-ba84-1b475144cf2f.png)
![Screenshot (16)](https://user-images.githubusercontent.com/47227715/154835538-da6ca2d1-08f6-428a-926a-33b9924ba530.png)

## Inventory Page

Inventory Page gives option for editing inventory of different products, searching product inventory by brand, category or barcode, editing ore or more product inventory together by uploading TSV file and downloading Inventory Report in TSV format.

![Screenshot (18)](https://user-images.githubusercontent.com/47227715/154835588-69d60b1e-da73-48ab-9028-3a1dc2e677ff.png)
![Screenshot (21)](https://user-images.githubusercontent.com/47227715/154835611-4c15d9f7-11c1-4569-a8e3-a00257b7408e.png)
![Screenshot (19)](https://user-images.githubusercontent.com/47227715/154835612-35d8d29c-3fe0-4603-b328-f6f09fa56053.png)
![Screenshot (20)](https://user-images.githubusercontent.com/47227715/154835617-37500e17-55a9-4fd4-b4ae-58a9272cbda6.png)

## Order Page

Order Page gives option for Adding orders for different order items, editing existing orders before generating invoice, viewing order item details for each order, generating Order Invoice for each order in PDF format and downloading Sales Report for different products.

![Screenshot (22)](https://user-images.githubusercontent.com/47227715/154835696-e26daa95-e228-4263-aae7-d0bb4920149d.png)
![Screenshot (23)](https://user-images.githubusercontent.com/47227715/154835697-2aa0525e-7e90-4b9b-bb85-18ac6669ae0c.png)
![Screenshot (25)](https://user-images.githubusercontent.com/47227715/154835699-236a0da6-7205-46c5-8ca3-88af97a135dc.png)
![Screenshot (26)](https://user-images.githubusercontent.com/47227715/154835703-4b21f06c-0241-4c3f-b891-47fce3df03fa.png)
![Screenshot (27)](https://user-images.githubusercontent.com/47227715/154835705-ec3501f2-10c2-4f16-be88-a6390eab3e42.png)
![Screenshot (24)](https://user-images.githubusercontent.com/47227715/154835710-64c75a46-6328-4bc0-a488-ba3ec02cc146.png)
