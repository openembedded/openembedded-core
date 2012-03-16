require xorg-driver-input.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "${INC_PR}.0"

SRC_URI[md5sum] = "15fce165117706cd5e774a8aa58122ce"
SRC_URI[sha256sum] = "a8a6ec0b567c48c130ccb830e15dfc2b201831841de0c2cc56bd87256d2d869a"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

do_install_append () {
	# We don't care about hal
	rm -rf ${D}${datadir}/hal/
	rm -rf ${D}${libdir}/hal/
}

FILES_${PN} += "${base_libdir}/udev/ ${datadir}/X11/xorg.conf.d"
