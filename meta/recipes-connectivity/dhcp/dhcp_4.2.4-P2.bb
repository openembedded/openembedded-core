require dhcp.inc

PR = "r0"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
           "

SRC_URI[md5sum] = "fe36056f2d274fa4b82a5422f192e65f"
SRC_URI[sha256sum] = "0f75170e323cd9573e6e09a5d9236725f3e56e3cac5a70a01fe2a9d76b436499"
