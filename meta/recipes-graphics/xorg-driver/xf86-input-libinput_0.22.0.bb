require xorg-driver-input.inc

SUMMARY = "Generic input driver for the X.Org server based on libinput"
LIC_FILES_CHKSUM = "file://COPYING;md5=5e6b20ea2ef94a998145f0ea3f788ee0"

DEPENDS += "libinput"

SRC_URI[md5sum] = "e4364319f15f97dc2ef0ef62c8616826"
SRC_URI[sha256sum] = "c762b4072ed448e2ae9f35cebd7fec8df7fd42b3ae61462cc3b2f720a873eae2"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
