SUMMARY = "A general-purpose utility library"
DESCRIPTION = "GLib is a general-purpose utility library, which provides many useful data types, macros, type conversions, string utilities, file utilities, a main loop abstraction, and so on."
# pcre is under BSD;
# docs/reference/COPYING is with a 'public domai'-like license!
LICENSE = "LGPLv2+ & BSD & PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://glib/glib.h;beginline=4;endline=17;md5=62b7bd0d17b98573dfb87495ac1c5b4c \
                    file://gmodule/COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gmodule/gmodule.h;beginline=4;endline=17;md5=62b7bd0d17b98573dfb87495ac1c5b4c \
                    file://glib/pcre/COPYING;md5=266ebc3ff74ee9ce6fad65577667c0f4 \
                    file://glib/pcre/pcre.h;beginline=11;endline=35;md5=de27f2bf633d20a2b7af0b1983423283 \
                    file://docs/reference/COPYING;md5=f51a5100c17af6bae00735cd791e1fcc"
BUGTRACKER = "http://bugzilla.gnome.org"
SECTION = "libs"

DEPENDS = "glib-2.0-native virtual/libiconv"
DEPENDS_class-native = "pkgconfig-native gettext-native"
DEPENDS_class-nativesdk = "nativesdk-libtool"
PACKAGES =+ "${PN}-utils ${PN}-bash-completion ${PN}-codegen"

LEAD_SONAME = "libglib-2.0.*"
FILES_${PN}-utils = "${bindir}/* ${datadir}/glib-2.0/gettext"

inherit autotools gettext gtk-doc pkgconfig ptest

S = "${WORKDIR}/glib-${PV}"

CORECONF = "--disable-dtrace --disable-fam --disable-libelf --disable-systemtap --disable-man"

PTEST_CONF = "${@base_contains('DISTRO_FEATURES', 'ptest', '--enable-modular-tests', '--disable-modular-tests', d)}"
EXTRA_OECONF = "--enable-included-printf=no ${CORECONF} ${PTEST_CONF}"
EXTRA_OECONF_class-native = "${CORECONF} --disable-selinux --disable-modular-tests"

FILES_${PN} = "${libdir}/lib*${SOLIBS} ${datadir}/glib-2.0/schemas \
               ${datadir}/glib-2.0/gettext/mkinstalldirs ${datadir}/glib-2.0/gettext/po/Makefile.in.in"
FILES_${PN}-dev += "${libdir}/glib-2.0/include \
                    ${libdir}/gio/modules/lib*${SOLIBSDEV} \
                    ${libdir}/gio/modules/*.la"
FILES_${PN}-dbg += "${datadir}/glib-2.0/gdb ${datadir}/gdb \
                    ${libdir}/gio/modules/.debug"
FILES_${PN}-codegen = "${datadir}/glib-2.0/codegen/*.py"
FILES_${PN}-bash-completion = "${sysconfdir}/bash_completion.d \
                               ${datadir}/bash-completion"

ARM_INSTRUCTION_SET = "arm"
USE_NLS = "yes"

do_install_append () {
	sed ${D}${bindir}/gtester-report -i -e '1s|^#!.*|#!/usr/bin/env python|'
}
