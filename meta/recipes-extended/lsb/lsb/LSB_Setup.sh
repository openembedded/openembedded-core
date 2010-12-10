#!/bin/bash

# Copyright (c) 2005-2010 Wind River Systems, Inc.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

##Prepare Steps
#Steps 0; Confirm the installed LSB Packages

if [ ! -f /opt/lsb/test/manager/bin/dist-checker-start.pl ]
then
        if [ -d /lsb-dist-testkit ];then
                cd /lsb-dist-testkit && sh install.sh
        else 
                echo "Please install the realted LSB Packages"
                exit 1
        fi
fi

#Steps 1; Deleted existed user tester
id tester
if [ $? -eq  0 ]
then 
        echo "User tester was existed"
        echo -n "Deleted tester(yes/no):"
        read INPUT
case $INPUT in 
yes|y)
        sleep 1
        userdel -rf tester
        if [ $? -eq 0 ] || [ $? -eq 6 ]
        then
                echo "Success to delete user tester"
        else
                echo "Fail to delete user tester"
        fi
        ;;
no|n)
        sleep 1
        echo "There must be deleted User test before ran LSB4 on Target"
        echo ""
        exit 1
        ;;
*)
        sleep 1
        echo "Input ERROR, pls reinput that your expected"
        echo ""
        exit 1
esac
else 
        echo "There was not User tester"
fi

##Funs
check ()
{
if [ $? -eq 0 ]
then
        echo "PASS"
else 
        echo "FAIL"
        exit 1
fi
}

###Start 
#Step 1:Add tester group
echo ""
echo "---------------------------------"
echo "Step 1:Add Group tester"
groupadd tester
check


#Step 2:Add User tester 
echo ""
echo "---------------------------------"
echo "Step 2:Add User tester"
useradd -g tester tester
check 

echo "Check the tester user"
id tester
check 

#Step 3;Stop Boa server
#echo ""
#echo "---------------------------------"
#echo "Step 3:Stop BOA server"
#/etc/init.d/boa stop
#check

#Step 4:Create Dirnames file for RPM
echo ""
echo "---------------------------------"
echo "Step 4:Create the Dirnames on target"

mkdir -pv /etc/rpm/sysinfo 

cat > /etc/rpm/sysinfo/Dirnames << EOF
/etc/opt/lsb
/home/tet/LSB.tools
/opt/lsb-tet3-lite/lib/ksh
/opt/lsb-tet3-lite/lib/perl
/opt/lsb-tet3-lite/lib/posix_sh
/opt/lsb-tet3-lite/lib/tet3
/opt/lsb-tet3-lite/lib/xpg3sh
/opt/lsb/appbat/lib/python2.4/site-packages/qm
/opt/lsb/appbat/lib/python2.4/site-packages/qm/external
/opt/lsb/appbat/lib/python2.4/site-packages/qm/external/DocumentTemplate
/opt/lsb/appbat/lib/python2.4/site-packages/qm/test
/opt/lsb/appbat/lib/python2.4/site-packages/qm/test/classes
/opt/lsb/appbat/lib/python2.4/site-packages/qm/test/web
/opt/lsb/test/doc
/opt/lsb/test/lib
/opt/lsb/test/qm/diagnostics
/opt/lsb/test/qm/doc
/opt/lsb/test/qm/doc/test/html
/opt/lsb/test/qm/doc/test/print
/opt/lsb/test/qm/dtml
/opt/lsb/test/qm/dtml/test
/opt/lsb/test/qm/messages/test
/opt/lsb/test/qm/tutorial/test/tdb
/opt/lsb/test/qm/tutorial/test/tdb/QMTest
/opt/lsb/test/qm/web
/opt/lsb/test/qm/web/images
/opt/lsb/test/qm/web/stylesheets
/opt/lsb/test/qm/xml
/opt/lsb/test/share
/usr/share/doc/lsb-runtime-test
/var/opt/lsb
/opt/lsb/test/desktop
/opt/lsb/test/desktop/fontconfig
/opt/lsb/test/desktop/freetype
/opt/lsb/test/desktop/gtkvts
/opt/lsb/test/desktop/libpng
/opt/lsb/test/desktop/qt3
/opt/lsb/test/desktop/xft
/opt/lsb/test/desktop/xml
/opt/lsb/test/desktop/xrender


EOF

if [ -f /etc/rpm/sysinfo/Dirnames ]
then
        echo "Success to creat Dirnames file"
else 
        echo "Fail to creat Dirnames file"
fi

#Step 5;
echo ""
echo "---------------------------------"
echo "Step 5:"
ldconfig -v
check;

#Step 6;
echo ""
echo "---------------------------------"
echo "Step 6:Check with link to ftp.linux-foundation.org"
echo "140.211.169.59 ftp.linux-foundation.org ftp.linuxfoundation.org" >> /etc/hosts

ping -c 5 ftp.linux-foundation.org
check

#Step 7
if [ -f /lib/modules/*-wr-standard/kernel/drivers/block/loop.ko ];then
	inmod /lib/modules/*-wr-standard/kernel/drivers/block/loop.ko
fi

#Step 8
echo ""
if [ -f /opt/lsb/test/manager/bin/dist-checker-start.pl ];then
        ./opt/lsb/test/manager/bin/dist-checker-start.pl
fi


#Step 9 get ip address for target platform
addr=`ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}'|sed s/[[:space:]]Bcast//g`
echo -e "you should input ${addr}:8888 on your browse"
#Step 8
echo "Done!!"

###End
