require dhcp.inc

SRC_URI += "file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
            file://fixsepbuild.patch \
            file://dhclient-script-drop-resolv.conf.dhclient.patch \
            file://replace-ifconfig-route.patch \
           "

SRC_URI[md5sum] = "1020d77e1a4c1f01b76279caff9beb80"
SRC_URI[sha256sum] = "a7b6517d5cf32c5e49d2323a63de00efe5391df7cb0045dfa0ec8f6ee46ebe8a"
