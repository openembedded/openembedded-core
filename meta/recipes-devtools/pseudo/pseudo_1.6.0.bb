require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "78c3f8aa8efe8cf15a2b21261174e3b6"
SRC_URI[sha256sum] = "e24f526443b31c3292ec5ba04950d230b5388e8983c7e192e9e489c007f3dba3"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async"
