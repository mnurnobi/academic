#!/bin/bash

if type -p java; then

	echo found java executable in PATH

	cd bin

	rmic mybooks.com.shared.BookSeller
	rmic mybooks.com.shared.BookSellerMain
	rmic mybooks.com.shared.TimeSyncOperator
	rmic mybooks.com.shared.BookSellerSub

	java mybooks.com.test.Tester >../testlog.txt
	
	cd ../


else
	echo "Unable to find Java"
fi
