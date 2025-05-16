require util-linux.inc

inherit autotools gettext pkgconfig

S = "${WORKDIR}/util-linux-${PV}"

EXTRA_AUTORECONF += "--exclude=gtkdocize"
EXTRA_OECONF += "--disable-all-programs --enable-libuuid"

LICENSE = "BSD-3-Clause"

do_install:append() {
	rm -rf ${D}${datadir} ${D}${bindir} ${D}${base_bindir} ${D}${sbindir} ${D}${base_sbindir} ${D}${exec_prefix}/sbin
}

BBCLASSEXTEND = "native nativesdk"
