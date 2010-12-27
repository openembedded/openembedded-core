require libmatchbox.inc

PV = "1.7+svnr${SRCPV}"
PR = "r5"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
           file://configure_fixes.patch;patch=1 \
	   file://check.m4 \
	   file://16bppfixes.patch;patch=1 \
	   file://matchbox-start-fix.patch;patch=1"

S = "${WORKDIR}/libmatchbox"

do_configure_prepend () {
        cp ${WORKDIR}/check.m4 ${S}/
}
