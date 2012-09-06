#!/bin/sh
# bash test script running in qemu 
#
# Author: veera <veerabrahmamvr@huawei.com>
#
# This file is licensed under the GNU General Public License,
# Version 2.
#

which bash
if [ $? -eq 0 ]; then
 echo "QEMU: bash is exist in the target by default"
 exit 0 
else
 echo "QEMU: No bash command in the qemu target"
 exit 1
fi
