require dhcp.inc

PR = "r1"

SRC_URI += "file://fixincludes.patch \
            file://dhcp-3.0.3-dhclient-dbus.patch;striplevel=0 \
            file://fix-external-bind.patch \
            file://link-with-lcrypto.patch \
            file://fixsepbuild.patch \
           "

SRC_URI[md5sum] = "6489e919ac093d17249270ee9be1020e"
SRC_URI[sha256sum] = "771a5cffb1fd1392d25926e22e1c58a006e2ad02ecd77d136096e5e366a5b6bc"
