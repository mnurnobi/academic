#!/bin/bash

if type -p java; then

	echo found java executable in PATH

	cd bin

	rmic mybooks.com.shared.BookSeller	

	java mybooks.com.server.Server

	cd ../

else
	echo "Unable to find Java"
fi
