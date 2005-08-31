#!/bin/sh

#
# Noodles' simpler update script. SL-C3000 only for the moment.
#

DATAPATH=$1
TMPPATH=/tmp/update
TMPDATA=$TMPPATH/tmpdata.bin
TMPHEAD=$TMPPATH/tmphead.bin

WFLG_KERNEL=0
WFLG_INITRD=0
WFLG_HDD=0

RO_MTD_LINE=`cat /proc/mtd | grep "root" | tail -n 1`
if [ "$RO_MTD_LINE" = "" ]; then
    RO_MTD_LINE=`cat /proc/mtd | grep "\<NAND\>.*\<2\>" | tail -n 1`
fi
RO_MTD_NO=`echo $RO_MTD_LINE | cut -d: -f1 | cut -dd -f2`
RO_MTD_SIZE_HEX=`echo $RO_MTD_LINE | cut -d" " -f2`
RO_MTD=/dev/mtd$RO_MTD_NO
RO_MTDBLK=/dev/mtdblock$RO_MTD_NO
RO_MTD_SIZE=`dc 0x$RO_MTD_SIZE_HEX 1024 /`

RW_MTD_LINE=`cat /proc/mtd | grep "home" | tail -n 1`
if [ "$RW_MTD_LINE" = "" ]; then
    RW_MTD_LINE=`cat /proc/mtd | grep "\<NAND\>.*\<2\>" | tail -n 1`
fi
RW_MTD_NO=`echo $RW_MTD_LINE | cut -d: -f1 | cut -dd -f2`
RW_MTD_SIZE_HEX=`echo $RW_MTD_LINE | cut -d" " -f2`
RW_MTD=/dev/mtd$RW_MTD_NO
RW_MTDBLK=/dev/mtdblock$RW_MTD_NO
RW_MTD_SIZE=`dc 0x$RW_MTD_SIZE_HEX 1024 /`

LOGOCAL_MTD=/dev/mtd1

VERBLOCK=0x48000
MVRBLOCK=0x70000

RESULT=0

Cleanup(){
	rm -f $VTMPNAME > /dev/null 2>&1
	rm -f $MTMPNAME > /dev/null 2>&1
	rm $CTRLPATH/* > /dev/null 2>&1
	rm $DATAPATH/* > /dev/null 2>&1
	exit $1
}
trap 'Cleanup 1' 1 15
trap '' 2 3

get_dev_pcmcia()
{
while read SOCKET CLASS DRIVER INSTANCE DEVS MAJOR MINOR;
do
    echo $DEVS
done
}
get_dev_pcmcia_slot()
{
    grep "^$1" /var/lib/pcmcia/stab | get_dev_pcmcia
}
sleep 1
IDE1=`get_dev_pcmcia_slot 1`
if [ "$IDE1" = "" ]; then
    echo "Error!! There is no HDD. Now retrying..."
    while [ "$IDE1" = "" ]; do
	IDE1=`get_dev_pcmcia_slot 1`
    done
    echo "Found HDD!!"
fi

#LINUXFMT=ext2
LINUXFMT=ext3
MKE2FSOPT=
if [ "$LINUXFMT" = "ext3" ]; then
	MKE2FSOPT=-j
fi


### Check model ###
/sbin/writerominfo
MODEL=`cat /proc/deviceinfo/product`
if [ "$MODEL" != "SL-C3000" ]
then
	echo 'MODEL:'$MODEL
	echo 'ERROR:Invalid model!'
	echo 'Please reset'
	while true
	do
	done
fi

mkdir -p $TMPPATH > /dev/null 2>&1

cd $DATAPATH/

#
# First do the kernel.
#
for TARGETFILE in zImage.bin zimage.bin ZIMAGE.BIN
do
	if [ -e $TARGETFILE -a $WFLG_KERNEL = 0 ]
	then
		# Get the size of the kernel.
		DATASIZE=`wc -c $TARGETFILE`
		DATASIZE=`echo $DATASIZE | cut -d' ' -f1`

		echo 'Updating kernel.'
		echo $TARGETFILE':'$DATASIZE' bytes'
		/sbin/nandlogical $LOGOCAL_MTD WRITE 0xe0000 $DATASIZE \
			$TARGETFILE > /dev/null 2>&1

		WFLG_KERNEL=1

	fi
done

#
# Now do the initrd.
#
for TARGETFILE in initrd.bin INITRD.BIN
do
	if [ -e $TARGETFILE -a $WFLG_INITRD = 0 ]
	then
		rm -f $TMPPATH/*.bin > /dev/null 2>&1
		DATASIZE=`wc -c $TARGETFILE`
		DATASIZE=`echo $DATASIZE | cut -d' ' -f1`

		WFLG_INITRD=1
		echo 'RO file system'
		MODULEID=6
		MODULESIZE=0x500000
		ADDR=0
		TARGET_MTD=$RO_MTD
		DATAPOS=16
		ONESIZE=1048576
		/sbin/bcut -s 16 -o $TMPHEAD $TARGETFILE
		
		echo -n 'Flash erasing...'
		/sbin/eraseall $TARGET_MTD 2> /dev/null > /dev/null
		echo 'done'

		echo ''
		echo '0%                      100%'
		PROGSTEP=`expr $DATASIZE / $ONESIZE + 1`
		PROGSTEP=`expr 28 / $PROGSTEP`
		if [ $PROGSTEP = 0 ]
		then
			PROGSTEP=1
		fi

		#00 means header information
		VTMPNAME=$TMPPATH'/vtmp'`date '+%s'`'.tmp'
		MTMPNAME=$TMPPATH'/mtmp'`date '+%s'`'.tmp'
		/sbin/nandlogical $LOGOCAL_MTD READ $VERBLOCK 0x4000 $VTMPNAME > /dev/null 2>&1
		/sbin/nandlogical $LOGOCAL_MTD READ $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1

		#echo 'found header'
		/sbin/verchg -v $VTMPNAME $TMPHEAD $MODULEID $MODULESIZE > /dev/null 2>&1
		/sbin/verchg -m $MTMPNAME $TMPHEAD $MODULEID $MODULESIZE > /dev/null 2>&1

		#loop
		while [ $DATAPOS -lt $DATASIZE ]
		do
			#data create
			bcut -a $DATAPOS -s $ONESIZE -o $TMPDATA $TARGETFILE
			TMPSIZE=`wc -c $TMPDATA`
			TMPSIZE=`echo $TMPSIZE | cut -d' ' -f1`
			DATAPOS=`expr $DATAPOS + $TMPSIZE`

			#handle data file
			#echo 'ADDR='$ADDR
			#echo 'SIZE='$TMPSIZE
			next_addr=`/sbin/nandcp -a $ADDR $TMPDATA $TARGET_MTD  2>/dev/null | fgrep "mtd address" | cut -d- -f2 | cut -d\( -f1`
			if [ "$next_addr" = "" ]; then
				echo "ERROR:flash write"
				rm $TMPDATA > /dev/null 2>&1
				RESULT=3
				break;
			fi
			ADDR=$next_addr

			rm $TMPDATA > /dev/null 2>&1

			#progress
			SPNUM=0
			while [ $SPNUM -lt $PROGSTEP ]
			do
				echo -n '.'
				SPNUM=`expr $SPNUM + 1`
			done
		done

		echo ''

		#finish
		rm -f $TMPPATH/*.bin > /dev/null 2>&1

		if [ $RESULT = 0 ]
		then
			/sbin/nandlogical $LOGOCAL_MTD WRITE $VERBLOCK 0x4000 $VTMPNAME > /dev/null 2>&1
			/sbin/nandlogical $LOGOCAL_MTD WRITE $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1

			rm -f $VTMPNAME > /dev/null 2>&1
			rm -f $MTMPNAME > /dev/null 2>&1
			echo 'Success!'
		else
			echo 'Error!'
			exit $RESULT
		fi
	fi
done

## HDD image
for TARGETFILE in hdimage1.tgz HDIMAGE1.TGZ
do
	if [ -e $TARGETFILE ]; then
		if [ $WFLG_HDD != 0 ]
		then
			continue
		fi
		WFLG_HDD=1
		echo ''
		echo 'HDD RO file system'
		if [ ! -f /hdd1/NotAvailable ]; then
		    umount /hdd1
		fi
		echo 'Now formatting...'
		mke2fs $MKE2FSOPT /dev/${IDE1}1 2> /dev/null > /dev/null
		e2fsck -p /dev/${IDE1}1 > /dev/null
		if [ "$?" != "0" ]; then
		    echo "Error!"
		    exit "$?"
		fi

		mount -t $LINUXFMT -o noatime /dev/${IDE1}1 /hdd1
		if [ "$?" != "0" ]; then
		    echo "Error!"
		    exit "$?"
		fi
    
		cd /hdd1
		echo 'Now extracting...'
		gzip -dc $DATAPATH/$TARGETFILE | tar xf -
		if [ "$?" != "0" ]; then
		    echo "Error!"
		    exit "$?"
		fi

		echo 'Success!'
		# remount as RO
		cd /
		umount /hdd1
		mount -t $LINUXFMT -o ro,noatime /dev/${IDE1}1 /hdd1
	fi
done

exit 0
