SRC_URI = "file://aclocal.tgz \
           file://COPYING"

INHIBIT_DEFAULT_DEPS = "1"
INHIBIT_AUTOTOOLS_DEPS = "1"

LICENSE = "FSF-Unlimited"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

inherit native

PR = "r1"

S = "${WORKDIR}"

do_install () {
	install -d ${D}${datadir}/aclocal/
	cp ${WORKDIR}/*.m4 ${D}${datadir}/aclocal/
}
