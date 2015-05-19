SUMMARY = "A lightweight web browser"
HOMEPAGE = "http://midori-browser.org/"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"
DEPENDS = "webkit-gtk libsoup-2.4 openssl python-native python-docutils-native librsvg-native libnotify libxscrnsaver"

SRC_URI = "https://launchpad.net/midori/trunk/0.5.8/+download/${BPN}-${PV}.tar.bz2;subdir=${BPN}-${PV} \
           file://liststore.patch"

SRC_URI[md5sum] = "b89e25e74199d705e74767499a415976"
SRC_URI[sha256sum] = "af19135fd4c4b04345df4d3592e7939c20f9b40eaca24550e6cb619751aa9381"

# midori depends on webkit-gtk, and webkit-gtk can NOT be built on
# MIPS64 with n32 ABI. So remove it from mips64 n32 temporarily.
COMPATIBLE_HOST_mips64n32 = "null"

inherit gtk-icon-cache pkgconfig vala pythonnative cmake distro_features_check

# The webkit-gtk and libxscrnsaver requires x11 in DISTRO_FEATURES
REQUIRED_DISTRO_FEATURES = "x11"

EXTRA_OECMAKE = " \
    -DCMAKE_INSTALL_PREFIX=${prefix} \
    -DUSE_ZEITGEIST=0 \
"

TARGET_CC_ARCH += "${LDFLAGS}"

RRECOMMENDS_${PN} += "glib-networking ca-certificates gnome-icon-theme"

FILES_${PN} += "${datadir}/appdata"
