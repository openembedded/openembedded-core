include libmatchbox.inc

PV = "1.6cvs${CVSDATE}"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http\
	   file://check.m4"
S = "${WORKDIR}/libmatchbox"

do_configure_prepend () {
        mv ${WORKDIR}/check.m4 ${S}/
}
