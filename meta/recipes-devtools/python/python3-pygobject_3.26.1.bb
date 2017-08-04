SUMMARY = "Python GObject bindings"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"

inherit autotools pkgconfig gnomebase distutils3-base gobject-introspection upstream-version-is-even

DEPENDS += "python3 glib-2.0"

SRCNAME="pygobject"
SRC_URI = " \
    http://ftp.gnome.org/pub/GNOME/sources/${SRCNAME}/${@gnome_verdir("${PV}")}/${SRCNAME}-${PV}.tar.xz \
"

SRC_URI[md5sum] = "d898fc768ebec7a9ed4e4270c6fabfde"
SRC_URI[sha256sum] = "f5577b9b9c70cabb9a60d81b855d488b767c66f867432e7fb64aa7269b04d1a9"

S = "${WORKDIR}/${SRCNAME}-${PV}"


PACKAGECONFIG ??= "${@bb.utils.contains_any('DISTRO_FEATURES', [ 'directfb', 'wayland', 'x11' ], 'cairo', '', d)}"

# python3-pycairo is checked on configuration -> DEPENDS
# we don't link against python3-pycairo -> RDEPENDS
PACKAGECONFIG[cairo] = "--enable-cairo,--disable-cairo,cairo python3-pycairo, python3-pycairo"

RDEPENDS_${PN} += "python3-setuptools"

BBCLASSEXTEND = "native"
PACKAGECONFIG_class-native = ""
