#!/bin/sh

# Do not run when pcmcia-cs is installed
test -x /sbin/cardctl && exit 0

# We get two "add" events for hostap cards due to wifi0
echo "$INTERFACE" | grep -q wifi && exit 0

#
# Code taken from pcmcia-cs:/etc/pcmcia/network
#

# if this interface has an entry in /etc/network/interfaces, let ifupdown
# handle it
if grep -q "iface \+$INTERFACE" /etc/network/interfaces; then
  case $ACTION in
    add)
    	ifup $INTERFACE
    	;;
    remove)
    	ifdown $INTERFACE
    	;;
  esac
  
  exit 0
fi
