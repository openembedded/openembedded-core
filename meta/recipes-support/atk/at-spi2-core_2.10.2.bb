SUMMARY = "Assistive Technology Service Provider Interface (dbus core)"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "d4a198201f6189fb248f3edd19efe39c"
SRC_URI[sha256sum] = "d3da58f84f4c8e4d5fe940ecb52fb27b4d9ea2b4dcdb3e1fae0f46b5eaa2dde1"

DEPENDS = "dbus glib-2.0 virtual/libx11 libxi libxtst intltool-native"

inherit autotools gtk-doc

EXTRA_OECONF = "--disable-introspection --disable-xevie"

FILES_${PN} += "${datadir}/dbus-1/services/*.service"
