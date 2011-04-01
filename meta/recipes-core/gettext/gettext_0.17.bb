SUMMARY = "Utilities and libraries for producing multi-lingual messages."
DESCRIPTION = "Gettext offers to programmers, translators, and even users, a well integrated set of tools and documentation. Specifically, the GNU `gettext' utilities are a set of tools that provides a framework to help other GNU packages produce multi-lingual messages. These tools include a set of conventions about how programs should be written to support message catalogs, a directory and file naming organization for the message catalogs themselves, a runtime library supporting the retrieval of translated messages, and a few stand-alone programs to massage in various ways the sets of translatable strings, or already translated strings."
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"
SECTION = "libs"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PR = "r5"
DEPENDS = "gettext-native virtual/libiconv ncurses expat"
DEPENDS_virtclass-native = ""
PROVIDES = "virtual/libintl"
PROVIDES_virtclass-native = ""

SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz \
           file://autotools.patch;patch=1 \
           file://wchar-uclibc.patch;patch=1 \
           file://use_open_properly.patch;patch=1 \
           file://m4fix.patch;patch=1 \
          "

SRC_URI_append_linux-uclibc = " file://gettext-error_print_progname.patch;patch=1"
SRC_URI_append_linux-uclibceabi = " file://gettext-error_print_progname.patch;patch=1"

SRC_URI[md5sum] = "58a2bc6d39c0ba57823034d55d65d606"
SRC_URI[sha256sum] = "209638bb8e162f22c281145a34e220c66f1f6e9ff5e4c50c6f2ef2ded59537ba"

#PARALLEL_MAKE = ""

inherit autotools

EXTRA_OECONF += "--without-lispdir \
                 --disable-csharp \
                 --disable-libasprintf \
                 --disable-java \
                 --disable-native-java \
                 --disable-openmp \
                 --with-included-glib \
                 --with-libncurses-prefix=${STAGING_LIBDIR}/.. \
                 --without-emacs \
                "

acpaths = '-I ${S}/autoconf-lib-link/m4/ \
           -I ${S}/gettext-runtime/m4 \
           -I ${S}/gettext-tools/m4'


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

BBCLASSEXTEND = "native nativesdk"
