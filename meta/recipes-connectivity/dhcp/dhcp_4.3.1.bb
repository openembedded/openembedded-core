require dhcp.inc

SRC_URI += "file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
            file://fixsepbuild.patch \
            file://dhclient-script-drop-resolv.conf.dhclient.patch \
            file://replace-ifconfig-route.patch \
            file://dhcp-xen-checksum.patch \
           "

SRC_URI[md5sum] = "b3a42ece3c7f2cd2e74a3e12ca881d20"
SRC_URI[sha256sum] = "266cbca8a7a6bb8f9ccc5765da0d2b04099329314a54a4fc1022d510ad3e9af0"
