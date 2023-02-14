SUMMARY = "A thin layer of graphic data types"
HOMEPAGE = "http://ebassi.github.io/graphene/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a7d871d9e23c450c421a85bb2819f648"

GNOMEBASEBUILDCLASS = "meson"

inherit gnomebase gobject-introspection gtk-doc

SRC_URI[archive.sha256sum] = "a37bb0e78a419dcbeaa9c7027bcff52f5ec2367c25ec859da31dfde2928f279a"

# gtk4 & mutter 41.0 requires graphene build with introspection
# Disable neon support by default on ARM-32 platforms because of the
# following upstream bug: https://github.com/ebassi/graphene/issues/215
PACKAGECONFIG ?= "introspection ${@bb.utils.contains('TUNE_FEATURES', 'aarch64', 'neon', '', d)}"

PACKAGECONFIG[introspection] = "-Dintrospection=enabled,-Dintrospection=disabled,"
PACKAGECONFIG[neon] = "-Darm_neon=true,-Darm_neon=false,"

GTKDOC_MESON_OPTION = "gtk_doc"

EXTRA_OEMESON = "-Dinstalled_tests=false"

FILES:${PN} += "${libdir}/graphene-1.0"

BBCLASSEXTEND = "native nativesdk"
