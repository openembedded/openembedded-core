require dhcp.inc

PR = "r1"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
           "

SRC_URI[md5sum] = "14f57fd580d01633d0fad4809007a801"
SRC_URI[sha256sum] = "5cf7ae2cad9c4ca0103748b2476ec8ea78484e408f8fe597e4e0a4afb051b469"
