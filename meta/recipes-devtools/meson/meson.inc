HOMEPAGE = "http://mesonbuild.com"
SUMMARY = "A high performance build system"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "https://github.com/mesonbuild/meson/releases/download/${PV}/meson-${PV}.tar.gz \
           file://0001-gtkdoc-fix-issues-that-arise-when-cross-compiling.patch \
           file://0002-gobject-introspection-determine-g-ir-scanner-and-g-i.patch \
           file://0003-native_bindir.patch \
           file://0001-python-module-do-not-manipulate-the-environment-when.patch \
           file://disable-rpath-handling.patch \
           file://cross-prop-default.patch \
           file://load-configs-generalise-search-path.patch \
           file://0007-mesonbuild-allow-multiple-cross-file-options.patch \
           file://0001-environment.py-detect-windows-also-if-the-system-str.patch \
           file://0001-mesonbuild-environment.py-do-not-determine-whether-a.patch \
           file://0001-mesonbuild-environment.py-check-environment-for-vari.patch \
           file://0001-modules-python.py-do-not-substitute-python-s-install.patch \
           "
SRC_URI[sha256sum] = "2a1bc42dda58206fb922cda5e1ca95cc03ad126321d26acc47d3493ec4e7021f"
SRC_URI[md5sum] = "433483107fda4616eaf33de7e7083a84"

SRC_URI_append_class-native = " \
    file://0001-Make-CPU-family-warnings-fatal.patch \
    file://0002-Support-building-allarch-recipes-again.patch \
"

UPSTREAM_CHECK_URI = "https://github.com/mesonbuild/meson/releases"

inherit setuptools3

RDEPENDS_${PN} = "ninja python3-core python3-modules"

FILES_${PN} += "${datadir}/polkit-1"
