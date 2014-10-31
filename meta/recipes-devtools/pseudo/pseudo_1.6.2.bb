require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "4d7b4f9d1b4aafa680ce94a5a9a52f1f"
SRC_URI[sha256sum] = "c72be92689511ced7c419149c6aaa1b1a9e4dfc6409d1f16ab72cc35bc1e376a"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async"
