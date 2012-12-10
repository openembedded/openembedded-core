#!/bin/bash
# smart test script running in target
#
# Author: Jiajun Xu <jiajun.xu@intel.com>
#
# This file is licensed under the GNU General Public License,
# Version 2.
#

Target_Info()
{
	echo -e "\tTARGET: $*"
}

Target_Err()
{
	echo -e "\tTARGET: smart command has issue when running, Pls. check the error log"
	echo -e "\tTARGET: ##### Error Log #####"
	$1
	echo -e "\tTARGET: #####    End    #####"
}

which smart
if [ $? -ne 0 ]; then
	Target_Info "No smart command found"
	exit 1
fi

if [ smart > /dev/null 2>&1 ]; then
	Target_Info "smart command run without problem"
else
	Target_Err smart
	exit 1
fi

# run smart with specific command parsed to smart_test.sh
smart $* > /dev/null 2>&1

if [ $? -eq 0 ]; then
	Target_Info "smart $* work without problem"
	exit 0
else
	Target_Err "smart $*"
	exit 1
fi
