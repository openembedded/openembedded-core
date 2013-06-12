require dhcp.inc

PR = "r1"

SRC_URI += "file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
            file://fixsepbuild.patch \
           "

SRC_URI[md5sum] = "f68e3c1f00a9af5742bc5e71d567cf93"
SRC_URI[sha256sum] = "59b06c9f5d775e46999b422c45b9229402c462b114ce1685617bfb2b8b028250"
