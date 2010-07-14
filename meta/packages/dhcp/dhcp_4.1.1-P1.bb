require dhcp4.inc

PR = "r0"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0"
