CREATE DATABASE printer_distributor;
#DROP DATABASE printer_distributor;
USE printer_distributor;

DELETE FROM bills WHERE id >= 1;
DELETE FROM suppliers WHERE id >= 1;
DELETE FROM products WHERE id_product >= 1;
DELETE FROM sales WHERE id >= 1;

SELECT * FROM bills;
SELECT * FROM suppliers;
SELECT * FROM products;
SELECT * FROM sales;

INSERT INTO bills(`date`,total) VALUES 
("2021-10-14",436000),
("2021-10-15",1012000),
("2021-10-16",436000),
("2021-10-17",1012000),
("2021-10-18",436000),
("2021-10-19",1012000),
("2021-10-20",436000),
("2021-10-21",1012000),
("2021-10-22",436000),
("2021-10-23",1012000),
("2021-10-24",436000),
("2021-10-25",1012000),
("2021-10-26",436000),
("2021-10-27",1012000);

INSERT INTO suppliers(is_presale,last_review,lead_time,`name`,phone_number,review_period) VALUES 
(false,now(),7,"HP",4955555,10),
(true,now(),7,"EPSON",4956666,10);

INSERT INTO products(avg_demand,cost_of_preparing,cost_unit,`description`,dis_demand, model, model_type, reorder_point, scan,service_level,stock,storage_cost,supplier_id) VALUES 
(2.0,600,17000,"better impressions for your reports",1.0,"Laser 107a",0,17, "00AA1",1,25,100,1),
(4.0,500,22000,"pure quality",2.0,"Laser 109b",0,22, "00AA2",1,30,150,1),
(3.0,500,32000,"smart impressions",2.0,"Smart Tank 515",0,null, "00AA3",1,35,100,1),
(6.0,1000,52000,"very fast impressions",3.0,"L4160 TINTA INAL",1,null, "00AA4",1,40,150,2),
(2.0,500,37000,"smart and fast impressions",1.0,"Ecotank LL3210",1,null, "00AA5",1,50,150,2),
(3.0,600,40000,"quality impressions",2.0,"Ecotank L3210",1,null, "00AA6",1,55,150,2);

select * from products;
select * from bills;

INSERT INTO sales(`date`,quantity,subtotal,id_product,id_bill) VALUES 
("2021-10-14",4,68000,1,1),
("2021-10-14",8,17600,2,1),
("2021-10-14",6,19200,3,1),

("2021-10-15",12,624000,4,2),
("2021-10-15",4,148000,5,2),
("2021-10-15",6,240000,6,2),

("2021-10-16",4,68000,1,3),
("2021-10-16",8,17600,2,3),
("2021-10-16",6,19200,3,3),

("2021-10-17",12,624000,4,4),
("2021-10-17",4,148000,5,4),
("2021-10-17",6,240000,6,4),

("2021-10-18",4,68000,1,5),
("2021-10-18",8,17600,2,5),
("2021-10-18",6,19200,3,5),

("2021-10-19",12,624000,4,6),
("2021-10-19",4,148000,5,6),
("2021-10-19",6,240000,6,6),

("2021-10-20",4,68000,1,7),
("2021-10-20",8,17600,2,7),
("2021-10-20",6,19200,3,7),

("2021-10-21",12,624000,4,8),
("2021-10-21",4,148000,5,8),
("2021-10-21",6,240000,6,8),

("2021-10-22",4,68000,1,9),
("2021-10-22",8,17600,2,9),
("2021-10-22",6,19200,3,9),

("2021-10-23",12,624000,4,10),
("2021-10-23",4,148000,5,10),
("2021-10-23",6,240000,6,10),

("2021-10-24",4,68000,1,11),
("2021-10-24",8,17600,2,11),
("2021-10-24",6,19200,3,11),

("2021-10-25",12,624000,4,12),
("2021-10-25",4,148000,5,12),
("2021-10-25",6,240000,6,12),

("2021-10-26",4,68000,1,13),
("2021-10-26",8,17600,2,13),
("2021-10-26",6,19200,3,13),

("2021-10-27",12,624000,4,14),
("2021-10-27",4,148000,5,14),
("2021-10-27",6,240000,6,14);

select * from products;

