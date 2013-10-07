SUMMARY = "Assistive Technology Service Provider Interface (dbus core)"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "bb3b6f5679ecfc7dabcf76a1b6bfe2db"
SRC_URI[sha256sum] = "964155c7574220a00e11e1c0d91f2d3017ed603920eb1333ff9cbdb6a22744db"

DEPENDS = "dbus glib-2.0 virtual/libx11 libxi libxtst intltool-native"

inherit autotools gtk-doc

EXTRA_OECONF = "--disable-introspection --disable-xevie"

FILES_${PN} += "${datadir}/dbus-1/services/*.service"
