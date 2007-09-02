#! /bin/sh
/etc/init.d/hwclock.sh stop

# Update the timestamp
date +%2m%2d%2H%2M%Y > /etc/timestamp
