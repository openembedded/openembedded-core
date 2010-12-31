#!/bin/bash
# rpm test script running in target
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
	echo -e "\tTARGET: rpm command has issue when running, Pls. check the error log"
	echo -e "\tTARGET: ##### Error Log #####"
	$1
	echo -e "\tTARGET: #####    End    #####"
}

which rpm
if [ $? -ne 0 ]; then
	Target_Info "No rpm command found"
	exit 1
fi

if [ rpm > /dev/null 2>&1 ]; then
	Target_Info "rpm command run without problem"
else
	Target_Err rpm
	exit 1
fi

# run rpm with specific command parsed to rpm_test.sh
rpm $* > /dev/null 2>&1

if [ $? -eq 0 ]; then
	Target_Info "rpm $* work without problem"
	exit 0
else
	Target_Err rpm $*
	exit 1
fi
