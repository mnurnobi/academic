#!/bin/bash

if type -p java; then

	echo found java executable in PATH

	cd bin	

	rmiregistry 4099 &	

	cd ../

else
	echo "Unable to find Java"
fi
