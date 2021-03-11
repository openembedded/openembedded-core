# To allow util-linux to optionally build-depend on cryptsetup, libuuid is
# split out of the main recipe, as it's needed by cryptsetup

require util-linux.inc

inherit autotools gettext pkgconfig

S = "${WORKDIR}/util-linux-${PV}"
EXTRA_OECONF += "--disable-all-programs --enable-libuuid"
PACKAGES = "util-linux-libuuid util-linux-libuuid-dev util-linux-libuuid-staticdev util-linux-libuuid-dbg"
FILES_util-linux-libuuid = "${libdir}/libuuid.so.*"
FILES_util-linux-libuuid-dev = "${libdir}/libuuid.so ${includedir} ${libdir}/pkgconfig"
FILES_util-linux-libuuid-staticdev = "${libdir}/libuuid.a"
FILES_util-linux-libuuid-dbg = "/usr/src ${libdir}/.debug"

do_install_append() {
	rm -rf ${D}${datadir} ${D}${bindir} ${D}${base_bindir} ${D}${sbindir} ${D}${base_sbindir} ${D}${exec_prefix}/sbin
}

BBCLASSEXTEND = "native nativesdk"
