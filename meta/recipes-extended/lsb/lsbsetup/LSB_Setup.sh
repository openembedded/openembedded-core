#!/bin/bash

# Copyright (C) 2010-2011 Wind River Systems, Inc.
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
# Confirm the installed LSB Packages
ARCH=`uname -m`
APP_FILE=`ls /lsb-Application/*.rpm`
if [ ! -f /opt/lsb/test/manager/bin/dist-checker-start.pl ]
then
        if [ -d /lsb-dist-testkit ];then
		if [ ${ARCH} == i686 ];then
                	echo "i486-suse" >> /etc/rpm/platform
                	echo "i486-noarch" >> /etc/rpm/platform
                	echo "i486-pc" >> /etc/rpm/platform
                	echo "noarch-suse" >> /etc/rpm/platform
		else
                	echo "${ARCH}-suse" >> /etc/rpm/platform
                	echo "${ARCH}-noarch" >> /etc/rpm/platform
                	echo "${ARCH}-pc" >> /etc/rpm/platform
                	echo "noarch-suse" >> /etc/rpm/platform
                fi      
                cd /lsb-dist-testkit && sh install.sh && cd ../lsb-Application
                for i in ${APP_FILE}
                do
                    echo "$i" |grep -q "apache"
                    if [ $? -eq 0 ]
                    then
                        rpm -ivh $i --noscripts --nodeps --force
                    else
                        rpm -ivh $i --nodeps --force
                    fi
                done
                mkdir -p /var/opt/lsb/test/manager/packages/ftp.linuxfoundation.org/pub/lsb/snapshots/appbat/tests/
                mkdir -p  /var/opt/lsb/test/manager/packages/ftp.linuxfoundation.org/pub/lsb/app-battery/tests/ 
		cp expect-tests.tar  test1.pdf  test2.pdf /var/opt/lsb/test/manager/packages/ftp.linuxfoundation.org/pub/lsb/app-battery/tests/
                cp raptor-tests.tar  tcl-tests.tar /var/opt/lsb//test/manager/packages/ftp.linuxfoundation.org/pub/lsb/snapshots/appbat/tests/
		cd ..
        else 
                echo "Please install the realted LSB Packages"
                exit 1
        fi
fi

# Deleted existed user tester
id tester
if [ $? -eq  0 ]
then 
        echo "User tester was existed"
        sleep 1
        userdel -rf tester
        if [ $? -eq 0 ] || [ $? -eq 6 ]
        then
                echo "Success to delete user tester"
       else
                echo "Fail to delete user tester"
        fi
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
# Add tester group
echo ""
echo "---------------------------------"
echo "Step 1:Add Group tester"
groupadd tester
check


# Add User tester 
echo ""
echo "---------------------------------"
echo "Step 2:Add User tester"
useradd -g tester tester
check 

echo "Check the tester user"
id tester
check 

# Stop Boa server
#echo ""
#echo "---------------------------------"
#echo "Step 3:Stop BOA server"
#/etc/init.d/boa stop
#check

# Create Dirnames file for RPM
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

# ldconfig 
echo ""
echo "---------------------------------"
echo "Step 5:"
ldconfig -v
check;


# Insmod look.ko
insmod /lib/modules/2.6.*/kernel/drivers/block/loop.ko
if [ $? != 0 ];then
	echo "Please insmod loop.ko  manully"
fi
# Start lsb 
echo ""
if [ -f /opt/lsb/test/manager/bin/dist-checker-start.pl ];then
        /opt/lsb/test/manager/bin/dist-checker-start.pl
fi

# Get ip address for target platform
addr=`ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}'|sed s/[[:space:]][[:space:]]Bcast//g`
echo -e "you should input ${addr}:8888 on your browser"

# Workaround to add part of locales for LSB test
localedef -i ja_JP -f EUC-JP ja_JP.eucjp
localedef -i en_US -f ISO-8859-15  en_US.ISO-8859-15
localedef -i en_US -f ISO-8859-1  en_US.ISO-8859-1
localedef -i en_US -f ISO-8859-1 en_US
localedef -i de_DE -f UTF-8  de_DE.UTF-8
localedef -i de_DE@euro -f ISO-8859-15 de_DE@euro
localedef -i en_US -f UTF-8 en_US.UTF-8
localedef -i se_NO -f UTF-8 se_NO.UTF-8
localedef -i de_DE -f ISO-8859-1 de_DE
localedef -i de_DE -f ISO-8859-15 de_DE.ISO-8859-15
localedef -i en_US -f ISO-8859-1 en_US.ISO8859-1
localedef -i fr_FR -f ISO-8859-1 fr_FR
localedef -i it_IT -f ISO-8859-1 it_IT
localedef -i es_MX  -f ISO-8859-1 es_MX
localedef -i en_HK -f ISO-8859-1 en_HK
localedef -i en_PH -f ISO-8859-1 en_PH
localedef -i ta_IN -f UTF-8 ta_IN

# Resolve localhost 
LOCALHOST=`hostname`
if ! `grep -F -q "$LOCALHOST" /etc/hosts`; then
    echo "127.0.0.1 $LOCALHOST" >> /etc/hosts
fi

# Start avahi-daemon                                      
/etc/init.d/avahi-daemon start                           
ln -s /etc/init.d/avahi-daemon /etc/rc5.d/S88avahi-daemon 

# Done
echo "Done!!"

###End


