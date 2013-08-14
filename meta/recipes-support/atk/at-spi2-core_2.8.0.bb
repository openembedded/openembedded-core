SUMMARY = "Assistive Technology Service Provider Interface (dbus core)"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "93b57d5d56d15d1222ddf2386e2f869f"
SRC_URI[sha256sum] = "1861a30fc7f583d5a567a0ba547db67ce9bd294f0d1c9f7403c96a10a481c458"

DEPENDS = "dbus glib-2.0 virtual/libx11 libxi libxtst intltool-native"

inherit autotools gtk-doc

EXTRA_OECONF = "--disable-introspection --disable-xevie"

FILES_${PN} += "${datadir}/dbus-1/services/*.service"
