SUMMARY = "Assistive Technology Service Provider Interface (dbus core)"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz \
           file://core_acinclude_m4.patch \
          "

SRC_URI[md5sum] = "b12ad0e0924706f5e7f51216241068ef"
SRC_URI[sha256sum] = "db550edd98e53b4252521459c2dcaf0f3b060a9bad52489b9dbadbaedad3fb89"

DEPENDS = "dbus glib-2.0 virtual/libx11 libxi libxtst intltool-native"

inherit autotools gtk-doc pkgconfig

EXTRA_OECONF = "--disable-introspection --disable-xevie"

FILES_${PN} += "${datadir}/dbus-1/services/*.service"
