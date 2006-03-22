include libmatchbox.inc

PV = "1.7+svn${SRCDATE}"
DEFAULT_PREFERENCE = "-1"
PR="1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http\
	   file://check.m4 \
	   file://16bppfixes.patch;patch=1"
S = "${WORKDIR}/libmatchbox"

do_configure_prepend () {
        mv ${WORKDIR}/check.m4 ${S}/
}
