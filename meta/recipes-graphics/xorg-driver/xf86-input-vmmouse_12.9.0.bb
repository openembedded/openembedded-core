require xorg-driver-input.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "${INC_PR}.0"

SRC_URI[md5sum] = "2b3bfea9ba1f73d9d68bddd0d6b20112"
SRC_URI[sha256sum] = "fbcf00f6bfee38bc65e0f0b812a4d076f7e203e81ed908e57de4026792b299bf"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

do_install_append () {
	# We don't care about hal
	rm -rf ${D}${datadir}/hal/
	rm -rf ${D}${libdir}/hal/
}

FILES_${PN} += "${base_libdir}/udev/ ${datadir}/X11/xorg.conf.d"
