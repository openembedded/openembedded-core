#!/bin/sh
#
# Start sreadahead, of the config file exists

if [ -e /etc/readahead.packed ]; then
    /sbin/sreadahead
fi
