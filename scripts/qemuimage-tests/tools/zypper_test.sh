#!/bin/bash
# zypper test script running in target
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
	echo -e "\tTARGET: zypper command has issue when running, Pls. check the error log"
	echo -e "\tTARGET: ##### Error Log #####"
	$1
	echo -e "\tTARGET: #####    End    #####"
}

which zypper
if [ $? -ne 0 ]; then
	Target_Info "No zypper command found"
	exit 1
fi

if [ zypper > /dev/null 2>&1 ]; then
	Target_Info "zypper command run without problem"
else
	Target_Err zypper
	exit 1
fi

# run zypper with specific command parsed to zypper_test.sh
zypper $* > /dev/null 2>&1

if [ $? -eq 0 ]; then
	Target_Info "zypper $* work without problem"
	exit 0
else
	Target_Err zypper $*
	exit 1
fi
