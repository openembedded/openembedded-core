SUMMARY = "Stub implementation of gtk-doc"
DESCRIPTION = "Stub implementation of gtk-doc, as we don't want to build the API documentation"
SECTION = "x11/base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PROVIDES = "gtk-doc gobject-introspection-stub"

SRCREV = "3dfd0a09de696ec8c544762747f8a0f77153622e"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.gnome.org/${BPN}"

S = "${WORKDIR}/git"

inherit autotools

FILES_${PN} += "${datadir}"

BBCLASSEXTEND = "native"
