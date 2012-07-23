SUMMARY = "OpenEmbedded RPM resolver utility"
DESCRIPTION = "OpenEmbedded RPM resolver - performs RPM database lookups in batches to avoid \
 repeated invocations of rpm on the command line."
DEPENDS = "rpm"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

SRC_URI = "file://rpmresolve.c"

S = "${WORKDIR}"

do_compile() {
	${CC} ${CFLAGS} -ggdb -I${STAGING_INCDIR}/rpm ${LDFLAGS} rpmresolve.c -o rpmresolve -lrpmbuild -lrpm -lrpmio -lrpmdb -lpopt
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 rpmresolve ${D}${bindir}
}

BBCLASSEXTEND = "native"
