require dhcp.inc

PR = "r0"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
           "

SRC_URI[md5sum] = "0ca7181024651f6323951d5498c8020b"
SRC_URI[sha256sum] = "d3baabef27fc006e1ce1c4e3d03d7e5c4b6a34d5a2f45fa47d69235ed25ad420"

