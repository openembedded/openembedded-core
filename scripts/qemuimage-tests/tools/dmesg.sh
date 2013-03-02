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

# For now, ignore mmci-pl18x errors on qemuarm which appeared
# from the 3.8 kernel and are harmless
dmesg | grep -v mmci-pl18x | grep -iq "error"
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
