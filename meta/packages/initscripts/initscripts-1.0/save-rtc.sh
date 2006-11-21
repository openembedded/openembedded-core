#! /bin/sh
#
# Copyright Matthias Hentges <devel@hentges.net> (c) 2006
# License: GPL (see http://www.gnu.org/licenses/gpl.txt for a copy of the license)
#
# Filename: save-rtc.sh
# Date: 03-Jul-06


# Update the timestamp, if there is already one
if test -e /etc/timestamp
then
	echo "Will restore RCT from /etc/timestamp on next boot"
	echo "Delete that file to disable this feature."
        date +%2m%2d%2H%2M%Y > /etc/timestamp
fi
