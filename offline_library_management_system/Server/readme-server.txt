Mybooks.com: An Online Book Store

User Guidelines:
-------------------

This project include three separate modules.
1. Server - Perform/ manage all kinds of back-end computation
2. Client - Allow all kinds of user operation including Search, Lookup, Book Order and different reports
3. Test - To ensure reliability of overall system

System requirements and configuration:
1. JDK 1.8 with proper environment variable
2. For custom IP and port, it is required to modify project config file.
config.properties file contains all the require configurations.
File Name: config.properties
Path: ~\src\mybooks\com\asset

#Main Server Properties goes here
serverhost=localhost //Main server IP
#Worker Server Properties goes here
workerservers=server1,server2,server3 //Worker server names
worker_server1_ip=localhost // IP of worker server1
worker_server1_obj=workerserver_1 //Object Instance of worker server 1
worker_server2_ip=localhost // IP of worker server2
worker_server2_obj=workerserver_2 //Object Instance of worker server 2
worker_server3_ip=localhost // IP of worker server3
worker_server3_obj=workerserver_3 //Object Instance of worker server 3
worker_main_server_ip=worker_server1_ip //Indicate server1 as Leader Server
discount_count=100 //To limit discount count for the book
#For all server RMI port
rmi_port=4099


Server : This module required to run first
—————————————————————————————————————

1. To compile and build the system-

make clean
make

2. Sequence to Run The Servers
	a) Start rmiregistry
		rmi_registry.sh
	b) Start Main Server
		main_server.sh
	c) Start Worker Server
		worker_server1.sh
		worker_server2.sh
	d) Start Leader Server
		worker_server3.sh

3. To stop the servers run following scripts
	stop_server.sh
