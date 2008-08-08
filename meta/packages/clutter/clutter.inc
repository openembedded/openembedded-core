DESCRIPTION = "Clutter graphics library"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

STDDEPENDS = "virtual/libx11 gtk-doc-native pango glib-2.0 libxfixes gtk+"
BASE_CONF = "--disable-gtk-doc ${@get_clutter_fpu_setting(bb, d)}"

DEPENDS = "${STDDEPENDS}"
EXTRA_OECONF = "${BASE_CONF}"

DEPENDS_zylonite = "${STDDEPENDS} libgles-zylonite tslib"
EXTRA_OECONF_zylonite = "${BASE_CONF} --with-flavour=eglnative"
PACKAGE_ARCH_zylonite = "${MACHINE_ARCH}"
LDFLAGS_append_zylonite = " -lnws -lstdc++ "

DEPENDS_mx31litekit = "${STDDEPENDS} libgles-mx31 tslib"
EXTRA_OECONF_mx31litekit = "${BASE_CONF} --with-flavour=eglnative"
PACKAGE_ARCH_mx31litekit = "${MACHINE_ARCH}"
LDFLAGS_append_mx31litekit = " -lpvrNULLWSEGL -lstdc++ "

DEPENDS_omap-3430ldp = "${STDDEPENDS} libgles-omap3 tslib"
EXTRA_OECONF_omap-3430ldp = "${BASE_CONF} --with-flavour=eglnative"
PACKAGE_ARCH_omap-3430ldp = "${MACHINE_ARCH}"

DEPENDS_omap-3430sdp = "${STDDEPENDS} libgles-omap3 tslib"
EXTRA_OECONF_omap-3430sdp = "${BASE_CONF} --with-flavour=eglnative"
PACKAGE_ARCH_omap-3430sdp = "${MACHINE_ARCH}"

DEPENDS_mx31ads = "${STDDEPENDS} libgles-mx31 tslib"
EXTRA_OECONF_mx31ads = "${BASE_CONF} --with-flavour=eglnative"
PACKAGE_ARCH_mx31ads = "${MACHINE_ARCH}"
LDFLAGS_append_mx31ads = " -lpvrNULLWSEGL -lstdc++ "

inherit autotools pkgconfig gtk-doc

PACKAGES =+ "clutter-examples"
FILES_clutter-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points
require clutter-fpu.inc

#Fix up some weirdness in the docs
do_configure_prepend() {
        sed -i s:doc/reference/Makefile::g ${S}/configure.ac
        sed -i s:doc::g ${S}/Makefile.am
}

do_stage () {
        autotools_stage_all
}
