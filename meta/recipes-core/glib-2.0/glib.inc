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

BBCLASSEXTEND = "native nativesdk"

DEPENDS = "glib-2.0-native virtual/libiconv libffi zlib"
DEPENDS_append_class-target = "${@base_contains('DISTRO_FEATURES', 'ptest', ' dbus', '', d)}"
DEPENDS_class-native = "pkgconfig-native gettext-native libffi-native zlib-native"
DEPENDS_class-nativesdk = "nativesdk-libtool nativesdk-libffi nativesdk-zlib ${BPN}-native"

PACKAGES =+ "${PN}-utils ${PN}-bash-completion ${PN}-codegen"

LEAD_SONAME = "libglib-2.0.*"
FILES_${PN}-utils = "${bindir}/* ${datadir}/glib-2.0/gettext"

inherit autotools gettext gtk-doc pkgconfig ptest

S = "${WORKDIR}/glib-${PV}"

CORECONF = "--disable-dtrace --disable-fam --disable-libelf --disable-systemtap --disable-man"

PTEST_CONF = "${@bb.utils.contains('PTEST_ENABLED', '1', '--enable-installed-tests', '--disable-installed-tests', d)}"
EXTRA_OECONF = "--enable-included-printf=no ${CORECONF} ${PTEST_CONF}"
EXTRA_OECONF_class-native = "${CORECONF} --disable-selinux"
EXTRA_OECONF_append_libc-uclibc = " --with-libiconv=gnu"

do_configure_prepend() {
	sed -i -e '1s,#!.*,#!${USRBINPATH}/env python,' ${S}/gio/gdbus-2.0/codegen/gdbus-codegen.in
}

FILES_${PN} = "${libdir}/lib*${SOLIBS} ${libdir}/gio ${datadir}/glib-2.0/schemas \
               ${datadir}/glib-2.0/gettext/mkinstalldirs ${datadir}/glib-2.0/gettext/po/Makefile.in.in"
FILES_${PN}-dev += "${libdir}/glib-2.0/include \
                    ${libdir}/gio/modules/lib*${SOLIBSDEV} \
                    ${libdir}/gio/modules/*.la"
FILES_${PN}-dbg += "${datadir}/glib-2.0/gdb ${datadir}/gdb \
                    ${libdir}/gio/modules/.debug \
                    ${libdir}/glib-2.0/installed-tests/glib/.debug"
FILES_${PN}-codegen = "${datadir}/glib-2.0/codegen/*.py"
FILES_${PN}-bash-completion = "${sysconfdir}/bash_completion.d \
                               ${datadir}/bash-completion"
FILES_${PN}-ptest += "${libdir}/glib-2.0/installed-tests \
                      ${datadir}/installed-tests/glib"

ARM_INSTRUCTION_SET = "arm"
USE_NLS = "yes"

do_install_append () {
	sed ${D}${bindir}/gtester-report -i -e '1s|^#!.*|#!/usr/bin/env python|'

	# Remove some unpackaged files
	rm -f ${D}${datadir}/glib-2.0/codegen/*.pyc
	rm -f ${D}${datadir}/glib-2.0/codegen/*.pyo

	# Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
	# for target as /usr/bin/perl, so fix it to /usr/bin/perl.
	if [ -f ${D}${bindir}/glib-mkenums ]; then
		sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/glib-mkenums
	fi
}

RDEPENDS_${PN}-ptest += "\
            gnome-desktop-testing \
            tzdata \
            tzdata-americas \
            tzdata-asia \
            tzdata-europe \
            tzdata-posix \
            python-pygobject \
            python-dbus \
            shared-mime-info \
           "

RDEPENDS_${PN}-ptest_append_libc-glibc = "\
            eglibc-gconv-utf-16 \
            eglibc-charmap-utf-8 \
            eglibc-gconv-cp1255 \
            eglibc-charmap-cp1255 \
            eglibc-gconv-utf-32 \
            eglibc-gconv-utf-7 \
            eglibc-gconv-euc-jp \
            eglibc-gconv-iso8859-1 \
            eglibc-gconv-iso8859-15 \
            eglibc-charmap-invariant \
            eglibc-localedata-translit-cjk-variants \
           "
