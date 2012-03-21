DESCRIPTION = "The GNU internationalization library."
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=9ea3144f04c41cd2eada5d3f472e6ea5"

PR = "r4"
DEPENDS = "virtual/libiconv"
DEPENDS_virtclass-native = ""
PROVIDES = "virtual/libintl virtual/gettext"
PROVIDES_virtclass-native = ""

SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz \
           file://gettext-vpath.patch \
           file://linklib_from_0.17.patch \
	   file://gettext-autoconf-lib-link-no-L.patch \
           file://disable_java.patch \
           file://fix_aclocal_version.patch \
           file://fix_gnu_source_circular.patch \
           file://hardcode_macro_version.patch \
          "


SRC_URI_append_linux-uclibc = " file://gettext-error_print_progname.patch"
SRC_URI_append_linux-uclibceabi = " file://gettext-error_print_progname.patch"

SRC_URI[md5sum] = "3d9ad24301c6d6b17ec30704a13fe127"
SRC_URI[sha256sum] = "0bf850d1a079fb5a61f0a47b1a9efd35eb44032255375e1cedb0253bc27b376d"

PARALLEL_MAKE = ""

inherit autotools

EXTRA_OECONF += "--without-lisp --disable-csharp --disable-openmp --without-emacs"
acpaths = '-I ${S}/autoconf-lib-link/m4/ \
           -I ${S}/gettext-runtime/m4 \
           -I ${S}/gettext-tools/m4'

do_configure_prepend() {
	rm -f ${S}/config/m4/libtool.m4
}

# these lack the .x behind the .so, but shouldn't be in the -dev package
# Otherwise you get the following results:
# 7.4M    glibc/images/ep93xx/Angstrom-console-image-glibc-ipk-2008.1-test-20080104-ep93xx.rootfs.tar.gz
# 25M     uclibc/images/ep93xx/Angstrom-console-image-uclibc-ipk-2008.1-test-20080104-ep93xx.rootfs.tar.gz
# because gettext depends on gettext-dev, which pulls in more -dev packages:
# 15228   KiB /ep93xx/libstdc++-dev_4.2.2-r2_ep93xx.ipk
# 1300    KiB /ep93xx/uclibc-dev_0.9.29-r8_ep93xx.ipk
# 140     KiB /armv4t/gettext-dev_0.14.1-r6_armv4t.ipk
# 4       KiB /ep93xx/libgcc-s-dev_4.2.2-r2_ep93xx.ipk

PACKAGES =+ "libgettextlib libgettextsrc"
FILES_libgettextlib = "${libdir}/libgettextlib-*.so*"
FILES_libgettextsrc = "${libdir}/libgettextsrc-*.so*"

do_install_append() {
	rm -f ${D}${libdir}/preloadable_libintl.so
}
