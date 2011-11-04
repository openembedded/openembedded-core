require dhcp.inc

PR = "r4"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-client-path.patch \
            file://fix-external-bind.patch \
           "

SRC_URI[md5sum] = "83abd7c4f9c24d8dd024ca5a71380c0a"
SRC_URI[sha256sum] = "6260d43423e4c406ba63cd7199502a395e952b13c80a955026c4b82a4e9d4943"
