DESCRIPTION = "The GNU internationalization library."
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r11"
DEPENDS = "virtual/libiconv"
DEPENDS_virtclass-native = ""
PROVIDES = "virtual/libintl"
PROVIDES_virtclass-native = ""

SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz \
           file://gettext-vpath.patch \
           file://fixchicken.patch \
           file://linklib_from_0.17.patch \
           file://getline.m4.patch \
           file://disable_java.patch"

#	   file://gettext-autoconf-lib-link-no-L.patch \

SRC_URI_append_linux-uclibc = " file://gettext-error_print_progname.patch"
SRC_URI_append_linux-uclibceabi = " file://gettext-error_print_progname.patch"

SRC_URI[md5sum] = "78f4b862510beb2e5d43223dd610e77d"
SRC_URI[sha256sum] = "41f20c469c7759acb34cfa7e0b3cac9096d59a58ad800e471f07424a8e7adbb7"

PARALLEL_MAKE = ""

inherit autotools

EXTRA_OECONF += "--without-lisp --disable-csharp"
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
