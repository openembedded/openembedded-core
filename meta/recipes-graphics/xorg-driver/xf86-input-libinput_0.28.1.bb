require xorg-driver-input.inc

SUMMARY = "Generic input driver for the X.Org server based on libinput"
LIC_FILES_CHKSUM = "file://COPYING;md5=5e6b20ea2ef94a998145f0ea3f788ee0"

DEPENDS += "libinput"

SRC_URI[md5sum] = "9d2fb3d6b452d568a275908b856de0e2"
SRC_URI[sha256sum] = "9ada448e076c0078a84b48e5298fa8ce317565f9b342b74c20429214a707d98b"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
