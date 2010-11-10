#!/bin/sh

SYSCTL_CONF="/etc/sysctl.conf"
if [ -f "${SYSCTL_CONF}" ]; then
	/sbin/sysctl -q -p "${SYSCTL_CONF}"
fi
