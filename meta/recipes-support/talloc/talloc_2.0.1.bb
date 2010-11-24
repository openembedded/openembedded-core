DESCRIPTION = "Hierarchical, reference counted memory pool system with destructors"
HOMEPAGE = "http://talloc.samba.org"
BUGTRACKER = "https://bugzilla.samba.org/"
LICENSE = "LGPLv3+"
LIC_FILES_CHKSUM = "file://talloc.c;beginline=8;endline=26;md5=ea9b6ec07a6d8b3e771f0ec75ca3a317"

PR = "r0"

SRC_URI = "http://samba.org/ftp/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "c6e736540145ca58cb3dcb42f91cf57b"
SRC_URI[sha256sum] = "5b810527405f29d54f50efd78bf2c89e318f2cd8bed001f22f2a1412fd27c9b4"

inherit autotools pkgconfig

# autoreconf doesn't work well while reconfiguring included libreplace
do_configure () {
       gnu-configize
       oe_runconf
}

TARGET_CC_ARCH += "${LDFLAGS}"

BBCLASSEXTEND = "native"
