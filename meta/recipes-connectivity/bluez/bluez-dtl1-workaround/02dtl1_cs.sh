#!/bin/sh

case "$1" in
	suspend)
		if [ "`/sbin/hciconfig`" != "" ]; then
			#If hciconfig outputs anything then there's probably a Bluetooth
			# CF card in the slot so shut it down.
			hcitool dc `hcitool con | grep ACL | sed 's/^.*\([0-9A-F]\{2\}\(:[0-9A-F]\{2\}\)\{5\}\).*$/\1/'`
			hciconfig hci0 down
			killall hciattach > /dev/null 2>&1 
		fi 
		;;

	resume)
		#check for kernel version
		if [ "`uname -r | grep 2.4.`" != "" ]; then
			k="o"
		elif [ "`uname -r | grep 2.6.`" != "" ]; then
			k="ko"
		else
			exit 0
		fi

		if test -e /sbin/cardctl; then
			CARDCTL=/sbin/cardctl
		elif test -e /sbin/pccardctl; then
			CARDCTL=/sbin/pccardctl
		else
			exit 0
		fi

		if [ "`lsmod | grep hci_uart`" != "" ]; then
			#If the hci_usb module is still loaded then there's a serial based
			# Bluetooth CF card in the slot, which only needs a resume to get it going
			# again. 
			rfcomm bind all
			$CARDCTL resume
			hciconfig hci0 up
		else
			# only works for nokia dtl1 cards
			for f in /lib/modules/`uname -r`/kernel/drivers/bluetooth/dtl1_cs.$k
			do
				#Enumerate all the self-contained Bluetooth CF card drivers
				f=`echo $f | sed 's/\.'$k'$//'`
				f=`basename $f`
				if [ "`lsmod | grep $f`" != "" ]; then
					#If one of these drivers is still loaded, then there is probably
					#a non-serial based Bluetooth CF card in the slot that needs
					#ejecting and reinserting to get it going again
					rfcomm bind all
					$CARDCTL eject
					$CARDCTL insert
					hciconfig hci0 up
				fi
			done
		fi
esac
