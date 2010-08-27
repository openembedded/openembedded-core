#!/bin/sh

PATH=/sbin:/bin:/usr/sbin:/usr/bin

DAEMON=/usr/sbin/wpa_supplicant
CONFIG="/etc/wpa_supplicant.conf"
PNAME="wpa_supplicant"

# insane defaults
OPTIONS=""

test -f /etc/default/wpa && . /etc/default/wpa

if [ ! -f $CONFIG ]; then
	echo "No configuration file found, not starting."
	exit 1
fi

test -f $DAEMON || exit 0

case "$1" in
	start)
		echo -n "Starting wpa_supplicant: "
		start-stop-daemon -S -b -x $DAEMON -- -Bw -c $CONFIG $OPTIONS >/dev/null
		echo "done."
		;;
	stop)
		echo -n "Stopping wpa_supplicant: "
		start-stop-daemon -K -n $PNAME >/dev/null
		echo "done."
		;;
	reload|force-reload)
		echo -n "Reloading wpa_supplicant: "
		killall -HUP $PNAME
		echo "done."
		;;
	restart)
		echo -n "Restarting wpa_supplicant: "
		start-stop-daemon -K -n $PNAME >/dev/null
		sleep 1
		start-stop-daemon -S -b -x $DAEMON -- -Bw -c $CONFIG $OPTIONS >/dev/null
		echo "done."
		;;
	*)
		echo "Usage: $0 {start|stop|restart|reload|force-reload}" >&2
		exit 1
		;;
esac

exit 0
