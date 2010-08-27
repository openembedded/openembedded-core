#!/bin/sh
#
# One updater.sh to rule them all
#
# 2006.10.24 Marcin 'Hrw' Juszkiewicz
# - started work on common updater.sh
# - works on poodle, c760, spitz
# - breaks on tosa
#
# 2007.10.08 Marcin 'Hrw' Juszkiewicz
# - do not allow to flash files bigger then partition size
# - created functions for common stuff
#
# 2007.11.18 Dmitry 'Lumag' Baryshkov
# - fixes
# - tosa unbreak
#
# 2007.11.19 Marcin 'Hrw' Juszkiewicz
# - size check unbreak
# - c760/c860 has bigger rootfs - use it
#
# 2007.11.23 Koen Kooi
# - consistent error messages
# - fix flashing from case sensitive filesystem (e.g. ext2)
#
# 2007.11.23 Matthias 'CoreDump' Hentges
# - Always treat MTD_PART_SIZE as HEX when comparing sizes
# - Thanks to ZeroChaos for debugging
#
# 2007.12.04 Matthias 'CoreDump' Hentges
# - Unb0rk flashing of Akita kernels
#
# 2007.12.10 Marcin 'Hrw' Juszkiewicz
# - Reformatted file - please use spaces not tabs
# - "version check" is only on Tosa and Poodle - breaks other machines
#
# 2007.12.23 Matthias 'CoreDump' Hentges
# - Fix kernel install on spitz machines
# - Unify format of do_flashing()...
# - Display ${PR} of zaurus-updater.bb to the user
# - Polish HDD installer messages
#
# 2007.12.25 Matthias 'CoreDump' Hentges
# -Add support for installing / updating u-boot

# Set to "yes" to enable
ENABLE_UBOOT_UPDATER="no"

DATAPATH=$1
TMPPATH=/tmp/update
TMPDATA=$TMPPATH/tmpdata.bin
TMPHEAD=$TMPPATH/tmphead.bin

FLASHED_KERNEL=0
FLASHED_ROOTFS=0
UNPACKED_ROOTFS=0   # spitz only

RO_MTD_LINE=`cat /proc/mtd | grep "root" | tail -n 1`
if [ "$RO_MTD_LINE" = "" ]; then
    RO_MTD_LINE=`cat /proc/mtd | grep "\<NAND\>.*\<2\>" | tail -n 1`
fi
RO_MTD_NO=`echo $RO_MTD_LINE | cut -d: -f1 | cut -dd -f2`
RO_MTD=/dev/mtd$RO_MTD_NO
ROOTFS_SIZE=`echo $RO_MTD_LINE | cut -d" " -f2`

LOGOCAL_MTD=/dev/mtd1

VERBLOCK=0x48000
MVRBLOCK=0x70000

RESULT=0

Cleanup()
{
    rm -f $VTMPNAME > /dev/null 2>&1
    rm -f $MTMPNAME > /dev/null 2>&1
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

check_for_hdd()
{
    IDE1=`get_dev_pcmcia_slot 1`
    if [ "$IDE1" = "" ]; then
        echo "Error: There is no microdrive. Retrying..."
        while [ "$IDE1" = "" ]; do
            IDE1=`get_dev_pcmcia_slot 1`
        done
        echo "Microdrive found."
    fi

    LINUXFMT=ext3
    MKE2FSOPT=-j
}

check_for_tar()
{
    ### Check that we have a valid tar
    for TARNAME in gnu-tar GNU-TAR
    do
        if [ -e $DATAPATH/$TARNAME ]
        then
            TARBIN=$DATAPATH/$TARNAME
        fi
    done

    if [ ! -e $TARBIN ]; then
        echo 'Error: Please place a valid copy of tar as "gnu-tar" on your card.'
        echo 'Please reset'
        while true
        do
        done
    fi
}

do_rootfs_extraction()
{
    UNPACKED_ROOTFS=1
    echo 'Installing HDD root file system'
    if [ ! -f /hdd1/NotAvailable ]; then
        umount /hdd1
    fi
    echo -n '* Now formatting...'
    mke2fs $MKE2FSOPT /dev/${IDE1}1 > /dev/null 2>&1
    e2fsck -p /dev/${IDE1}1 > /dev/null
    if [ "$?" != "0" ]; then
    	echo "FAILED"
        echo "Error: Unable to create filesystem on microdrive!"
        exit "$?"
    else 
    	echo "Done"
    fi

    mount -t $LINUXFMT -o noatime /dev/${IDE1}1 /hdd1
    if [ "$?" != "0" ]; then
        echo "Error: Unable to mount microdrive!"
        exit "$?"
    fi

    cd /hdd1
    echo -n '* Now extracting (this can take over 5m)...'
    gzip -dc $DATAPATH/$TARGETFILE | $TARBIN xf -
    if [ "$?" != "0" ]; then
    	echo "FAILED"
        echo "Error: Unable to extract root filesystem archive!"
        exit "$?"
    else
    	echo "Done"
    fi

    echo 'HDD Installation Finished.'

    # remount as RO
    cd /
    umount /hdd1
    mount -t $LINUXFMT -o ro,noatime /dev/${IDE1}1 /hdd1
}

do_flashing()
{
	if [ $DATASIZE -gt `printf "%d" $MTD_PART_SIZE` ]
	then
		echo "Error: File is too big to flash!"
		echo "$FLASH_TYPE: [$DATASIZE] > [`printf "%d" ${MTD_PART_SIZE}`]"
		return
	fi

	if [ "$ZAURUS" = "tosa" ] || [ "$ZAURUS" = "poodle" ]
	then
		#check version
		/sbin/bcut -s 6 -o $TMPDATA $TMPHEAD
		
		if [ `cat $TMPDATA` != "SHARP!" ] > /dev/null 2>&1
		then
		    #no version info...
		    rm -f $TMPHEAD > /dev/null 2>&1
		    DATAPOS=0
		fi
	fi

	if [ $ISFORMATTED = 0 ]
	then
		/sbin/eraseall $TARGET_MTD > /dev/null 2>&1
		ISFORMATTED=1
	fi

	if [ -e $TMPHEAD ]
	then
		VTMPNAME=$TMPPATH'/vtmp'`date '+%s'`'.tmp'
		MTMPNAME=$TMPPATH'/mtmp'`date '+%s'`'.tmp'
		/sbin/nandlogical $LOGOCAL_MTD READ $VERBLOCK 0x4000 $VTMPNAME > /dev/null 2>&1
		/sbin/nandlogical $LOGOCAL_MTD READ $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1

		/sbin/verchg -v $VTMPNAME $TMPHEAD $MODULEID $MTD_PART_SIZE > /dev/null 2>&1
		/sbin/verchg -m $MTMPNAME $TMPHEAD $MODULEID $MTD_PART_SIZE > /dev/null 2>&1
	fi

        # Looks like Akita and Spitz are unique when it comes to kernel flashing
        
       	if [ "$ZAURUS" = "akita" -o "$ZAURUS" = "c3x00" ] && [ "$FLASH_TYPE" = "kernel" ]
	then 
#               	echo $TARGETFILE':'$DATASIZE'bytes'
		echo ""
		echo -n "Installing SL-Cxx00 kernel..."
               	echo '                ' > /tmp/data
               	 test "$ZAURUS" = "akita" && /sbin/nandlogical $LOGOCAL_MTD WRITE 0x60100 16 /tmp/data > /dev/null 2>&1
               	/sbin/nandlogical $LOGOCAL_MTD WRITE 0xe0000 $DATASIZE $TARGETFILE > /dev/null 2>&1
               	 test "$ZAURUS" = "akita" && /sbin/nandlogical $LOGOCAL_MTD WRITE 0x21bff0 16 /tmp/data > /dev/null 2>&1     
		 echo "Done"
	else	
	
		echo ''
		echo '0%                   100%'
		PROGSTEP=`expr $DATASIZE / $ONESIZE + 1`
		PROGSTEP=`expr 25 / $PROGSTEP`
		
		if [ $PROGSTEP = 0 ]
		then
			PROGSTEP=1
		fi	
			
                #loop
                while [ $DATAPOS -lt $DATASIZE ]
                do
                        #data create
                        bcut -a $DATAPOS -s $ONESIZE -o $TMPDATA $TARGETFILE
                        TMPSIZE=`wc -c $TMPDATA`
                        TMPSIZE=`echo $TMPSIZE | cut -d' ' -f1`
                        DATAPOS=`expr $DATAPOS + $TMPSIZE`

                        #handle data file
                        if [ $ISLOGICAL = 0 ]
                        then
                                next_addr=`/sbin/nandcp -a $ADDR $TMPDATA $TARGET_MTD  2>/dev/null | fgrep "mtd address" | cut -d- -f2 | cut -d\( -f1`
                                if [ "$next_addr" = "" ]; then
                echo "Error: flash write"
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
		if [ -e $VTMPNAME ]
		then
		    /sbin/nandlogical $LOGOCAL_MTD WRITE $VERBLOCK 0x4000 $VTMPNAME > /dev/null 2>&1
		    rm -f $VTMPNAME > /dev/null 2>&1
		fi

		if [ -e $MTMPNAME ]
		then
		    /sbin/nandlogical $LOGOCAL_MTD WRITE $MVRBLOCK 0x4000 $MTMPNAME > /dev/null 2>&1
		    rm -f $MTMPNAME > /dev/null 2>&1
		fi
	
		[ "$FLASH_TYPE" != "kernel" ] && echo 'Done.'
	else
		echo 'Error!'
	fi
}

update_uboot() {
	# The flashing part of this function is based on pdaXrom's
	# updater.sh
	
	if test "$ENABLE_UBOOT_UPDATER" != "yes" -o -z "$1"
	then
		echo "u-boot updates not allowed."
		return
	fi

	echo ""
	echo "Installing u-boot bootloader:"

	DATASIZE=`wc -c $TARGETFILE`
	FSIZE=`echo $DATASIZE | cut -d' ' -f1`

	echo -n "* Creating backup ($FSIZE Bytes)..."
	if ( nandlogical /dev/mtd1 READ 0 $FSIZE /tmp/sharploader.bin ) > /dev/null 2>&1
	then
		echo "Ok"

		echo -n "* Flashing u-boot..."
		if ( nandlogical /dev/mtd1 WRITE 0 $FSIZE $1 ) > /dev/null 2>&1
		then
			echo "Success"
		else
			echo "FAILED"
			echo "ERROR: Installation of u-boot failed!"

			echo -n "* Trying to restore backup..."			
			if ( nandlogical /dev/mtd1 WRITE 0 $FSIZE /tmp/sharploader.bin ) > /dev/null 2>&1
			then
				echo "Success"
				echo "Your old bootloader has been restored"
			else
				echo "FAILED"
				echo "Sorry, it's NAND-Restore time for you =("
			fi
		fi
	else
		echo "FAILED"
		echo "Could not create backup, aborting!"
		echo "Your bootloader has not been altered in any way."
		exit 1
	fi		
}

### Check model ###
/sbin/writerominfo
MODEL=`cat /proc/deviceinfo/product`
case "$MODEL" in
    SL-B500|SL-5600) 
        ZAURUS='poodle'
        ;;
    SL-6000)
        ZAURUS='tosa'
        ;;
    SL-C1000) 
        ZAURUS='akita'
        ;;
    SL-C700|SL-C750|SL-7500|SL-C760|SL-C860)
        ZAURUS='c7x0'
        ;;
    SL-C3000|SL-C3100|SL-C3200)
        ZAURUS='c3x00'
        check_for_hdd
        check_for_tar
        ;;
    *)
        echo 'MODEL: '$MODEL 'is unsupported'
        echo ''
        echo 'Please reset'
        while true
        do
        done
        ;;
esac

clear
echo "---- Universal Zaurus Updater ZAURUS_UPDATER_VERSION ----"
echo 'MODEL: '$MODEL' ('$ZAURUS')'

mkdir -p $TMPPATH > /dev/null 2>&1

cd $DATAPATH/

for TARGETFILE in u-boot.bin U-BOOT.BIN zimage zImage zImage.bin zimage.bin ZIMAGE ZIMAGE.BIN initrd.bin INITRD.BIN hdimage1.tgz HDIMAGE1.TGZ
do
    if [ ! -e $TARGETFILE ]
    then
        continue
    fi

    rm -f $TMPPATH/*.bin > /dev/null 2>&1
    DATASIZE=`wc -c $TARGETFILE`
    DATASIZE=`echo $DATASIZE | cut -d' ' -f1`

    # make TARGETFILE lowercase
    TARGETFILE_LC=`echo $TARGETFILE|tr A-Z a-z`

    case "$TARGETFILE_LC" in

    zimage|zimage.bin)
        if [ $FLASHED_KERNEL != 0 ]
        then
            continue
        fi
        FLASHED_KERNEL=1
        ISLOGICAL=1
        MODULEID=5
        MTD_PART_SIZE=0x13C000
        ADDR=`dc 0xE0000`
        ISFORMATTED=1
        DATAPOS=0
        ONESIZE=524288
        HDTOP=`expr $DATASIZE - 16`
        /sbin/bcut -a $HDTOP -s 16 -o $TMPHEAD $TARGETFILE
        FLASH_TYPE="kernel"
        do_flashing
        FLASH_TYPE=""
        ;;

    initrd.bin)
        if [ $FLASHED_ROOTFS != 0 ]
        then
            continue
        fi
        echo 'root file system'
        FLASHED_ROOTFS=1
        ISLOGICAL=0
        MODULEID=6
        MTD_PART_SIZE="0x$ROOTFS_SIZE"
        ADDR=0
        ISFORMATTED=0
        TARGET_MTD=$RO_MTD
        DATAPOS=16
        ONESIZE=1048576
        /sbin/bcut -s 16 -o $TMPHEAD $TARGETFILE
        FLASH_TYPE="rootfs"
        do_flashing
        FLASH_TYPE=""
        ;;

    hdimage1.tgz)
        if [ $UNPACKED_ROOTFS = 0 ]
        then
        	do_rootfs_extraction
        fi
        ;;
    
    u-boot.bin)
    	if [ FLASHED_UBOOT != 1 ]
	then
		update_uboot "$TARGETFILE"
		FLASHED_UBOOT="1"
	fi
	;;	
	
    *)
        ;;
    esac
done

# reboot
exit 0

# bcut usage: bcut [OPTION] <input file>

# -a: start position
# -s: cut size
# -o: output file

# ModuleId informations used by verchg Sharp binary:
#
# 0 - master
# 1 - Maintaince
# 2 - Diagnostics
# 3 - rescue kernel
# 4 - rescue rootfs
# 5 - normal kernel
# 6 - normal rootfs
# 7 - /home/
# 8 - parameter (whatever it means)
#
