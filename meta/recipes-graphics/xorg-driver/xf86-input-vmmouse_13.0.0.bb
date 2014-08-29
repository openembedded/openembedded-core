require xorg-driver-input.inc

SUMMARY = "X.Org X server -- VMWare mouse input driver"
DESCRIPTION = "The vmmouse driver enables support for the special VMMouse \
protocol that is provided by VMware virtual machines to give absolute \
pointer positioning. The vmmouse driver is capable of falling back to the \
standard 'mouse' driver if a VMware virtual machine is not detected."

PR = "${INC_PR}.0"

SRC_URI += "file://always_include_config.h.patch"

SRC_URI[md5sum] = "34f9f64ee6a1a51fc8266a9af24e1e07"
SRC_URI[sha256sum] = "04cfb60366008d4db815c550d8fb8d0a4270c75fa7a20fa3bddc9ecbd355612c"

RDEPENDS_${PN} += "xf86-input-mouse"

LIC_FILES_CHKSUM = "file://COPYING;md5=622841c068a9d7625fbfe7acffb1a8fc"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

do_install_append () {
	# We don't care about hal
	rm -rf ${D}${datadir}/hal/
	rm -rf ${D}${libdir}/hal/
}

FILES_${PN} += "${base_libdir}/udev/ ${datadir}/X11/xorg.conf.d"
