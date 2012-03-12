require xorg-driver-input.inc

DESCRIPTION = "X.Org X server -- VMWare mouse input driver"
PR = "${INC_PR}.1"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"

SRC_URI[md5sum] = "dc77181330f983c7d0ec1ea1592c2ca7"
SRC_URI[sha256sum] = "00e5d527a0d97e6b2a6e8c519e1339427e66fa0a43af026858655c7c62bd9e35"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

do_install_append () {
	# We don't care about hal
	rm -rf ${D}${datadir}/hal/
	rm -rf ${D}${libdir}/hal/
}

FILES_${PN} += "${base_libdir}/udev/ ${datadir}/X11/xorg.conf.d"
