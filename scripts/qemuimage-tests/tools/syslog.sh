#!/bin/bash
# syslog test script running in qemu 
#
# Author: veera <veerabrahmamvr@huawei.com>
#
# This file is licensed under the GNU General Public License,
# Version 2.
#

ps aux | grep -w syslogd | grep -v grep
if [ $? -eq 0 ]; then
 echo "QEMU: syslogd is running by default"
 exit 0 
else
 echo "QEMU: syslogd is not running"
 exit 1
fi
