require dhcp4.inc

PR = "r1"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-client-path.patch \
            file://fix-external-bind.patch \
           "
