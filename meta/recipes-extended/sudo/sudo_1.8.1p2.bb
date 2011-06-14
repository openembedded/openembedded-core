require sudo.inc

PR = "r0"

SRC_URI = "http://ftp.sudo.ws/sudo/dist/sudo-${PV}.tar.gz \
           file://libtool.patch"

SRC_URI[md5sum] = "e8330f0e63b0ecb2e12b5c76922818cc"
SRC_URI[sha256sum] = "281f90c80547cf22132e351e7f61c25ba4ba9cf393438468f318f9a7884026fb"

EXTRA_OECONF += " --with-pam=no"
