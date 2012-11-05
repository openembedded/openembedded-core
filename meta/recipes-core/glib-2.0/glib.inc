SUMMARY = "A general-purpose utility library"
DESCRIPTION = "GLib is a general-purpose utility library, which provides many useful data types, macros, type conversions, string utilities, file utilities, a main loop abstraction, and so on."
# pcre is under BSD;
# docs/reference/COPYING is with a 'public domai'-like license!
LICENSE = "LGPLv2+ & BSD & PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://glib/glib.h;startline=4;endline=17;md5=a4332fe58b076f29d07c9c066d2967b6 \
                    file://gmodule/COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gmodule/gmodule.h;startline=4;endline=17;md5=76ab161b37202cd004073c42fac276ed \
                    file://glib/pcre/COPYING;md5=266ebc3ff74ee9ce6fad65577667c0f4 \
                    file://glib/pcre/pcre.h;startline=11;endline=35;md5=2ffb79f0a0933f282f4f36cda635683d \
                    file://docs/reference/COPYING;md5=f51a5100c17af6bae00735cd791e1fcc"
BUGTRACKER = "http://bugzilla.gnome.org"
SECTION = "libs"

DEPENDS = "glib-2.0-native virtual/libiconv"
DEPENDS_class-native = "pkgconfig-native gettext-native"
DEPENDS_class-nativesdk = "nativesdk-libtool"
PACKAGES =+ "${PN}-utils ${PN}-bash-completion ${PN}-codegen"

LEAD_SONAME = "libglib-2.0.*"
FILES_${PN}-utils = "${bindir}/* ${datadir}/glib-2.0/gettext"

inherit autotools gettext gtk-doc pkgconfig

S = "${WORKDIR}/glib-${PV}"

CORECONF = "--disable-dtrace --disable-fam --disable-libelf --disable-systemtap --disable-modular-tests"

EXTRA_OECONF = "--enable-included-printf=no ${CORECONF}"
EXTRA_OECONF_class-native = "${CORECONF}"
EXTRA_OECONF_linuxstdbase = "--enable-included-printf=no ${CORECONF}"

FILES_${PN} = "${libdir}/lib*${SOLIBS} ${datadir}/glib-2.0/schemas \
               ${datadir}/glib-2.0/gettext/mkinstalldirs ${datadir}/glib-2.0/gettext/po/Makefile.in.in"
FILES_${PN}-dev += "${libdir}/glib-2.0/include \
                    ${libdir}/gio/modules/lib*${SOLIBSDEV} \
                    ${libdir}/gio/modules/*.la"
FILES_${PN}-dbg += "${datadir}/glib-2.0/gdb ${datadir}/gdb \
                    ${libdir}/gio/modules/.debug"
FILES_${PN}-codegen = "${libdir}/gdbus-2.0/codegen/*.py"
FILES_${PN}-bash-completion = "${sysconfdir}/bash_completion.d \
                               ${datadir}/bash-completion"

ARM_INSTRUCTION_SET = "arm"
USE_NLS = "yes"

do_install_append () {
	if [ "${PN}" != "glib-2.0-native" ]; then
		sed ${D}${bindir}/gtester-report -i -e 's,${STAGING_BINDIR_NATIVE},${bindir},g'
	fi
}
