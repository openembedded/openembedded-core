SRC_URI = "file://aclocal.tgz \
           file://COPYING"

INHIBIT_DEFAULT_DEPS = "1"
INHIBIT_AUTOTOOLS_DEPS = "1"

LICENSE = "FSF-Unlimited"
LIC_FILES_CHKSUM = "file://COPYING;md5=0854da868a929923087141d9d7aba7d5"

inherit native

PR = "r2"

S = "${WORKDIR}"

do_install () {
	install -d ${D}${datadir}/aclocal/
	cp ${WORKDIR}/*.m4 ${D}${datadir}/aclocal/
}
