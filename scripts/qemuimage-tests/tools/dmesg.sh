#!/bin/bash
# Dmesg test script running in QEMU
#
# Author: Jiajun Xu <jiajun.xu@intel.com>
#
# This file is licensed under the GNU General Public License,
# Version 2.
#

which dmesg
if [ $? -ne 0 ]; then
	echo "QEMU: No dmesg command found"
	exit 1
fi

dmesg | grep -iq "error"
if [ $? -eq 0 ]; then
	echo "QEMU: There is some error log in dmesg:"
	echo "QEMU: ##### Error Log ######"
	dmesg | grep -i "error"
	echo "QEMU: #####    End     ######"
	exit 1
else
	echo "QEMU: No error log in dmesg"
	exit 0
fi
