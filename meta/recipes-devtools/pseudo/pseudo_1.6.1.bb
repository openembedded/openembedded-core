require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "c19e4811635d12f2a923e47061c7d336"
SRC_URI[sha256sum] = "de9cc755b786bfbe2d416d35fab340f2b8ef9904cb2801be3092eeb7446a7c37"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async"
