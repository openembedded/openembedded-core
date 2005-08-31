#!/bin/sh


DATAPATH=$1
TMPPATH=/tmp/update
TMPDATA=$TMPPATH/tmpdata.bin
TMPHEAD=$TMPPATH/tmphead.bin

WFLG_KERNEL=0
WFLG_INITRD=0
WFLG_MVERSION=0

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


### Check model ###
/sbin/writerominfo
MODEL=`cat /proc/deviceinfo/product`
echo 'MODEL:'$MODEL
case "$MODEL" in
    SL-C700) ;;
    SL-C750) ;;
    SL-C760) ;;
    SL-C860) ;;
    SL-C3100) ;;
    SL-B500) ;;
    SL-5600) ;;
    *)
	echo 'ERROR:Invalid model!'
	echo 'Please reset'
	while true
	do
	done
	;;
esac

mkdir -p $TMPPATH > /dev/null 2>&1

cd $DATAPATH/

for TARGETFILE in zImage.bin zimage.bin ZIMAGE.BIN initrd.bin INITRD.BIN mversion.bin MVERSION.BIN
do
	if [ -e $TARGETFILE ]
	then
		rm -f $TMPPATH/*.bin > /dev/null 2>&1
		DATASIZE=`wc -c $TARGETFILE`
		DATASIZE=`echo $DATASIZE | cut -d' ' -f1`

		#echo $TARGETFILE':'$DATASIZE'bytes'
		TARGETTYPE=Invalid
		case "$TARGETFILE" in
		zImage.bin) TARGETTYPE=Kernel;;
		zimage.bin) TARGETTYPE=Kernel;;
		ZIMAGE.BIN) TARGETTYPE=Kernel;;
		initrd.bin) TARGETTYPE=RoFs;;
		INITRD.BIN) TARGETTYPE=RoFs;;
		mversion.bin) TARGETTYPE=MasterVer;;
		MVERSION.BIN) TARGETTYPE=MasterVer;;
		*)
			continue
			;;
		esac

		case "$TARGETTYPE" in
		Kernel)
			if [ $WFLG_KERNEL != 0 ]
			then
				continue
			fi
			WFLG_KERNEL=1
			echo 'kernel'
			ISLOGICAL=1
			MODULEID=5
			MODULESIZE=0x13C000
			ADDR=`dc 0xE0000`
			ISFORMATTED=1
			DATAPOS=0
			ONESIZE=524288
			HDTOP=`expr $DATASIZE - 16`
			/sbin/bcut -a $HDTOP -s 16 -o $TMPHEAD $TARGETFILE
			;;
		RoFs)
			if [ $WFLG_INITRD != 0 ]
			then
				continue
			fi
			WFLG_INITRD=1
			echo 'RO file system'
			ISLOGICAL=0
			MODULEID=6
			MODULESIZE=0x1900000
			ADDR=0
			ISFORMATTED=0
			TARGET_MTD=$RO_MTD
			DATAPOS=16
			ONESIZE=1048576
			/sbin/bcut -s 16 -o $TMPHEAD $TARGETFILE
			;;
		MasterVer)
			if [ $WFLG_MVERSION != 0 ]
			then
				continue
			fi
			WFLG_MVERSION=1
			echo 'Master version'
			MTMPNAME=$TMPPATH'/mtmp'`date '+%s'`'.tmp'
			/sbin/nandlogical $LOGOCAL_MTD READ $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1
			/sbin/verchg -m $MTMPNAME $TARGETFILE 0 0 > /dev/null 2>&1
			/sbin/nandlogical $LOGOCAL_MTD WRITE $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1
			rm -f $MTMPNAME > /dev/null 2>&1
			echo 'Success!'
			continue
			;;
		*)
			continue
			;;
		esac


		#format?
		if [ $ISFORMATTED = 0 ]
		then
			echo -n 'Flash erasing...'
			/sbin/eraseall $TARGET_MTD 2> /dev/null > /dev/null
			#/sbin/eraseall $TARGET_MTD 2
			echo 'done'
			ISFORMATTED=1
		fi

		echo ''
		echo '0%                   100%'
		PROGSTEP=`expr $DATASIZE / $ONESIZE + 1`
		PROGSTEP=`expr 25 / $PROGSTEP`
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

                if [ "$MODEL" = "SL-C3100" ] && [ $TARGETTYPE = Kernel ]; then 
                    echo $TARGETFILE':'$DATASIZE'bytes'
	            echo '                ' > /tmp/data
		    /sbin/nandlogical $LOGOCAL_MTD WRITE 0x60100 16 /tmp/data > /dev/null 2>&1
                    /sbin/nandlogical $LOGOCAL_MTD WRITE 0xe0000 $DATASIZE $TARGETFILE > /dev/null 2>&1
		    /sbin/nandlogical $LOGOCAL_MTD WRITE 0x21bff0 16 /tmp/data > /dev/null 2>&1
		#loop
                else
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
			#echo 'TMPDATA='$TMPDATA
			if [ $ISLOGICAL = 0 ]
			then
				next_addr=`/sbin/nandcp -a $ADDR $TMPDATA $TARGET_MTD  2>/dev/null | fgrep "mtd address" | cut -d- -f2 | cut -d\( -f1`
				if [ "$next_addr" = "" ]; then
					echo "ERROR:flash write"
					rm $TMPDATA > /dev/null 2>&1
					RESULT=3
					break;
				fi
				ADDR=$next_addr
			else
				/sbin/nandlogical $LOGOCAL_MTD WRITE $ADDR $DATASIZE $TMPDATA > /dev/null 2>&1
				ADDR=`expr $ADDR + $TMPSIZE`
			fi

			rm $TMPDATA > /dev/null 2>&1

			#progress
			SPNUM=0
			while [ $SPNUM -lt $PROGSTEP ]
			do
				echo -n '.'
				SPNUM=`expr $SPNUM + 1`
			done
		done

                fi

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

exit 0
