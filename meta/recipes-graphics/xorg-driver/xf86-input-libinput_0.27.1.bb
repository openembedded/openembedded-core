require xorg-driver-input.inc

SUMMARY = "Generic input driver for the X.Org server based on libinput"
LIC_FILES_CHKSUM = "file://COPYING;md5=5e6b20ea2ef94a998145f0ea3f788ee0"

DEPENDS += "libinput"

SRC_URI[md5sum] = "bdad198a7a9f2ce2c1f90d5e6760462b"
SRC_URI[sha256sum] = "d4ad8dc5ad6f962a3f15f61ba9e9f8e37fa0b57eee9f484e2bd721d60ca72ee6"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
