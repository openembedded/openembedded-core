require cogl.inc

PR = "r1"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "http://source.clutter-project.org/sources/cogl/1.8/${BPN}-${PV}.tar.bz2 \
	   file://macro-versions.patch \
	   file://build_for_armv4t.patch"

SRC_URI[md5sum] = "3145cbf7ff162c33065ea4421c047e2f"
SRC_URI[sha256sum] = "8b647b51a4fa93034fcd74ffe86b3d4c919b0e54789108f6d065414e6162ab73"
