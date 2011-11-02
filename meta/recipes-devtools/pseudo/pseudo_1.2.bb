require pseudo.inc

PR = "r3"

SRC_URI = "http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://oe-config.patch \
           file://static_sqlite.patch"

SRC_URI[md5sum] = "a2819084bab7e991f06626d02cf55048"
SRC_URI[sha256sum] = "4749a22df687f44d24c26e97170d4781a1bd52d5ee092364a40877e4d96ff058"
