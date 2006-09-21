DESCRIPTION = "various benchmarning tests for X"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "GPL"
PR = "r0"

inherit autotools

SRC_URI = \
    "svn://svn.o-hand.com/repos/misc/trunk;module=fstests;proto=http"

S = "${WORKDIR}/fstests/tests"

