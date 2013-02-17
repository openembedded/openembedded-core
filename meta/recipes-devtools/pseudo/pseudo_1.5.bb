require pseudo.inc

PR = "r3"

SRC_URI = "http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "e735bc099f2b9fd6d3f152a8c71e6315"
SRC_URI[sha256sum] = "41a73c59296b9d48005e0f911dd1becf25ffc3ff4cf4268020f2332efcffbf49"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async"
