-- Create database
CREATE DATABASE JetSetters;
-- use databasecustomeraccount
USE JetSetters;
-- Make tables 
CREATE TABLE Houses (
	houseId int auto_increment primary key,
	name varchar(255) not null,
    cost decimal(10,2) not null,
    bedroomNum int not null
);

CREATE TABLE Hotel (
	hotelId int auto_increment primary key,
	name varchar(255) not null,
    vacancies int not null,
    bedroomNum int not null,
    basePricePerNight decimal(10,2) not null,
    maxOccupants int not null
);

CREATE TABLE EmployeeAccount(
	username varchar(255) not null, 
    password varchar(255) not null,
    empId int auto_increment primary key,    
    isManager boolean not null,
    salary decimal(10,2) not null,
	workNumber varchar(30) not null,
    name varchar(255) not null,
    address varchar(255) not null
);

CREATE TABLE customerAccount(
	customerId int auto_increment primary key,
	username varchar(255) not null, 
    password varchar(255) not null,
	balanceOwed decimal(10,2) not null,
	name varchar(255) not null,
    address varchar(255)
);

CREATE TABLE customerOrder(
	orderId int auto_increment primary key,
    customerId int,
    foreign key(customerId) references customerAccount(customerId),
    lodgeType varchar(30) not null,
    startDate date not null,
    endDate date not null,
    totalCost decimal(10,2) not null
);

CREATE TABLE hotelImg(
	hotelImgId int auto_increment primary key,
	image longblob not null,
    hotelId int,
    foreign key(hotelId) references Hotel(hotelId) on delete cascade
);


CREATE TABLE houseImg(
	houseImgId int auto_increment primary key,
	image longblob not null,
    houseId int,
    foreign key(houseId) references Houses(houseId) on delete cascade
);

-- Insert accounts
INSERT INTO employeeaccount(username, password,empId,isManager,salary,workNumber,name,address)
VALUES("Employee","ifxx|twu$",1,false,50000.00,"123-456-7890","Jane Doe","Company Address");
INSERT INTO employeeAccount(username, password,empId,isManager,salary,workNumber,name,address)
VALUES("Manager","ifxx|twu$",2,true,50000.00,"123-456-7890","John Doe","Company Address");
SELECT * FROM jetsetters.employeeaccount;

-- Insert lodges
INSERT INTO Houses(name, cost, bedroomNum)
VALUES("Blue House",12.10,812);
INSERT INTO Houses(name, cost, bedroomNum)
VALUES("Mansion",21.23,3);
INSERT INTO Houses(name, cost, bedroomNum)
VALUES("Big Home",10.61,2);
INSERT INTO Hotel(name,vacancies,bedroomNum,basePricePerNight,maxOccupants)
VALUES("4 seasons",20,83,9.00,12);
INSERT INTO Hotel(name,vacancies,bedroomNum,basePricePerNight,maxOccupants)
VALUES("956 Hotel",67,12,37.20,5);

SELECT * FROM jetsetters.customeraccount;
SELECT * FROM jetsetters.employeeaccount;
SELECT * FROM jetsetters.hotel;
SELECT * FROM jetsetters.houses;

-- Drop Database
-- DROP DATABASE JetSetters;