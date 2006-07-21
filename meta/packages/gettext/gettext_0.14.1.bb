DESCRIPTION = "The GNU internationalization library."
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"
SECTION = "libs"
LICENSE = "GPL"
PR = "r3"
PROVIDES = "virtual/libintl"

SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz \
	   file://gettext-vpath.patch;patch=1;pnum=1 \
	   file://fixchicken.patch;patch=1;pnum=1"

PARALLEL_MAKE = ""

inherit autotools

EXTRA_OECONF += "--without-lisp"
acpaths = '-I ${S}/autoconf-lib-link/m4/ \
	   -I ${S}/gettext-runtime/m4 \
	   -I ${S}/gettext-tools/m4'

do_configure_prepend() {
	rm -f ${S}/config/m4/libtool.m4
	install -m 0644 ${STAGING_DATADIR}/aclocal/libtool.m4 ${S}/config/m4/
}

do_stage () {
	autotools_stage_includes
	oe_libinstall -so -C gettext-tools/lib libgettextlib ${STAGING_LIBDIR}/
	oe_libinstall -so -C gettext-tools/src libgettextpo ${STAGING_LIBDIR}/
	oe_libinstall -so -C gettext-tools/src libgettextsrc ${STAGING_LIBDIR}/
	oe_libinstall -so -C gettext-tools/intl libintl ${STAGING_LIBDIR}/
	oe_libinstall -so -C gettext-runtime/lib libasprintf ${STAGING_LIBDIR}/
}
