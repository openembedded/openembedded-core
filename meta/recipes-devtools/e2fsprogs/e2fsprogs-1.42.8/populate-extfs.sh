#!/bin/sh

do_usage () {
	cat << _EOF
Usage: populate-extfs.sh <source> <device>
Create an ext2/ext3/ext4 filesystem from a directory or file

  source: The source directory or file
  device: The target device

_EOF
	exit 1
}

[ $# -ne 2 ] && do_usage

SRCDIR=${1%%/}
DEVICE=$2
DEBUGFS="debugfs"

{
	CWD="/"
	find $SRCDIR | while read FILE; do
                TGT="${FILE##*/}"
                DIR="${FILE#$SRCDIR}"

		# Skip the root dir
		[ ! -z "$DIR" ] || continue
		[ ! -z "$TGT" ] || continue

                DIR="$(dirname "$DIR")"

                # debugfs handles the quotation mark differently from other special marks like {
                # If FILE contains quotation marks in its name, then we have to replace " with ""
                # so that debugfs could correclty recognize them. In this script, we use the prefix
                # of D_ to denote the file names that should be used by debugfs.
                #
                # The usage of case statements here is to avoid performace impact.
                case $FILE in
                *\"*)
			D_FILE="$(echo $FILE | sed -e 's#\"#\"\"#g')"
			;;
                *)
			D_FILE="$FILE"
			;;
                esac

                case $DIR in
                *\"*)
			D_DIR="$(echo $DIR | sed -e 's#\"#\"\"#g')"
			;;
                *)
			D_DIR="$DIR"
			;;
                esac

                case $TGT in
                *\"*)
			D_TGT="$(echo $TGT | sed -e 's#\"#\"\"#g')"
			;;
                *)
			D_TGT="$TGT"
			;;
                esac

		if [ "$DIR" != "$CWD" ]; then
			echo "cd \"$D_DIR\""
			CWD="$DIR"
		fi

		# Only stat once since stat is a time consuming command
		STAT=$(stat -c "TYPE=\"%F\";DEVNO=\"0x%t 0x%T\";MODE=\"%f\";U=\"%u\";G=\"%g\";AT=\"%x\";MT=\"%y\";CT=\"%z\"" "$FILE")
		eval $STAT

		case $TYPE in
		"directory")
			echo "mkdir \"$D_TGT\""
			;;
		"regular file" | "regular empty file")
			echo "write \"$D_FILE\" \"$D_TGT\""
			;;
		"symbolic link")
			LINK_TGT=$(readlink "$FILE")
			D_LINK_TGT="$(echo $LINK_TGT | sed -e 's#\"#\"\"#g')"
			echo "symlink \"$D_TGT\" \"$D_LINK_TGT\""
			;;
		"block special file")
			echo "mknod \"$D_TGT\" b $DEVNO"
			;;
		"character special file")
			echo "mknod \"$D_TGT\" c $DEVNO"
			;;
		"fifo")
			echo "mknod \"$D_TGT\" p"
			;;
		*)
			echo "Unknown/unhandled file type '$TYPE' file: $FILE" 1>&2
			;;
		esac

		# Set the file mode
		echo "sif \"$D_TGT\" mode 0x$MODE"

		# Set uid and gid
		echo "sif \"$D_TGT\" uid $U"
		echo "sif \"$D_TGT\" gid $G"

		# Set atime, mtime and ctime
		AT=`echo $AT | cut -d'.' -f1 | sed -e 's#[- :]##g'`
		MT=`echo $MT | cut -d'.' -f1 | sed -e 's#[- :]##g'`
		CT=`echo $CT | cut -d'.' -f1 | sed -e 's#[- :]##g'`
		echo "sif \"$D_TGT\" atime $AT"
		echo "sif \"$D_TGT\" mtime $MT"
		echo "sif \"$D_TGT\" ctime $CT"
	done

	# Handle the hard links.
	# Save the hard links to a file, use the inode number as the filename, for example:
	# If a and b's inode number is 6775928, save a and b to /tmp/tmp.VrCwHh5gdt/6775928.
	INODE_DIR=`mktemp -d` || exit 1
	for i in `find $SRCDIR -type f -links +1 -printf 'INODE=%i###FN=%p\n'`; do
		eval `echo $i | sed 's$###$ $'`
		echo ${FN#$SRCDIR} >>$INODE_DIR/$INODE
	done
	# Use the debugfs' ln and "sif links_count" to handle them.
	for i in `ls $INODE_DIR`; do
		# The link source
		SRC="$(head -1 $INODE_DIR/$i)"
		D_SRC="$(echo $SRC | sed -e 's#\"#\"\"#g')"
		# Remove the files and link them again except the first one
		sed -n -e '1!p' $INODE_DIR/$i | while read TGT; do
			D_TGT="$(echo $TGT | sed -e 's#\"#\"\"#g')"
			echo "rm \"$D_TGT\""
			echo "ln \"$D_SRC\" \"$D_TGT\""
		done
		LN_CNT=`cat $INODE_DIR/$i | wc -l`
		# Set the links count
		echo "sif \"$D_SRC\" links_count $LN_CNT"
	done
	rm -fr $INODE_DIR
} | $DEBUGFS -w -f - $DEVICE 2>&1 1>/dev/null | grep '.*: .*'

if [ $? = 0 ]; then
    echo "Some error occured while executing [$DEBUGFS -w -f - $DEVICE]"
    exit 1
fi
