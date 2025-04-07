#!/bin/sh

# We get two "add" events for hostap cards due to wifi0
echo "$INTERFACE" | grep -q wifi && exit 0

# udevd does clearenv(). Export shell PATH to children.
export PATH

# udev should only trigger ifupdown for interfaces marked as allow-hotplug
# and with util-linux that is as simple as adding --allow=hotplug.
# Busybox unfortunately doesn't have this option.
# allow-hotplug is a pattern like eth0 /eth* /eth*/1 /eth*=eth.
# This function checks if INTERFACE matches an allow-hotplug pattern.

allow_hotplug() {
    allow_hotplug="$(sed -n -e 's/^allow-hotplug \+\([^= ]*\).*/\1/p' /etc/network/interfaces)"
    for pattern in $allow_hotplug; do
        options="$(echo $pattern | sed -n -e 's,^/\?[^ /]\+/\(.*\),\1,p')"
        value="$(echo $pattern | sed -n -e 's,^/\?\([^ /]\+\).*,\1,p')"
        interfaces="$(ls -d /sys/class/net/$value 2>/dev/null | xargs -r -n 1 basename)"
        if [ "$options" != "" ]; then
            interfaces="$(echo $interfaces | awk -v n=$options '{print $n }')"
        fi
        echo "$interfaces" | grep -w -q "$INTERFACE"
        if [ $? -eq 0 ]; then
            return 0
        fi
    done

    return 1
}

if ! allow_hotplug; then
    exit 0
fi

# if this interface has an entry in /etc/network/interfaces, let ifupdown
# handle it
if grep -q "iface \+$INTERFACE" /etc/network/interfaces; then
  case $ACTION in
    add)
    	ip addr show dev "$INTERFACE" up | grep -q "$INTERFACE" || ifup $INTERFACE
    	;;
    remove)
    	ifdown $INTERFACE
    	;;
  esac
  
  exit 0
fi
