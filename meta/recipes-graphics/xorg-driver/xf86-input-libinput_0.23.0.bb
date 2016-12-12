require xorg-driver-input.inc

SUMMARY = "Generic input driver for the X.Org server based on libinput"
LIC_FILES_CHKSUM = "file://COPYING;md5=5e6b20ea2ef94a998145f0ea3f788ee0"

DEPENDS += "libinput"

SRC_URI[md5sum] = "56ff43915a273c787ea8b93ccb83f835"
SRC_URI[sha256sum] = "0b53ebdfe8f8fc7554dd92af1b1c3088a6d3ec4ae1a33fc76f57d635c736a9dc"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
