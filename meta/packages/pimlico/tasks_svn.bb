require tasks.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.10+svnr${SRCREV}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
        file://tasks-single.diff;patch=1 \
        file://tasks-owl.diff;patch=1"
