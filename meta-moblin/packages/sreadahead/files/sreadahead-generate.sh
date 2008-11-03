#!/bin/sh
#

if [ -e /etc/readahead.packed ]; then
    exit 0
fi

if [ -e /etc/readahead.packed.first ]; then
    mv /etc/readahead.packed.first /etc/readahead.packed.second
    exit 0
fi

# That's our second boot, we can generate the sreadahead file list
if [ -e /etc/readahead.packed.second ]; then
    rm -f /etc/readahead.packed.second
    find / -type f > filelist.txt

    /sbin/generate_filelist filelist.txt
    rm filelist.txt
    mv readahead.packed /etc/
fi
