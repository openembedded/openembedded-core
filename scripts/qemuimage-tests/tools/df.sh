#!/bin/bash
# df test script to check enough disk space for qemu target
#
# Author: veera <veerabrahmamvr@huawei.com>
#
# This file is licensed under the GNU General Public License,
# Version 2.
#taking the size of the each partition
array_list=(`df -P | tr -s " " | cut -d " " -f4`)
#Total size of the array
array_size=`echo ${#array_list[@]}`
loop_val=1
#while loop to check the size of partitions are less than 5MB
while [ $loop_val -lt  $array_size ]
do 
 #taking each value from the array to check the size 
 value=`echo ${array_list[$loop_val]}`
 if [[ $value -gt 5120 ]];then
  loop_val=`expr $loop_val + 1`
 else
  echo "QEMU: df : disk space is not enough"
  exit 1
 fi 
done
exit 0
