# The Boost web site provides free peer-reviewed portable
# C++ source libraries. The emphasis is on libraries which
# work well with the C++ Standard Library. The libraries are
# intended to be widely useful, and are in regular use by
# thousands of programmers across a broad spectrum of applications.
HOMEPAGE = "http://www.boost.org/"
LICENSE = "BSL-1.0 & MIT & Python-2.0"
LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

BOOST_VER = "${@"_".join(d.getVar("PV", True).split("."))}"
BOOST_MAJ = "${@"_".join(d.getVar("PV", True).split(".")[0:2])}"
BOOST_P = "boost_${BOOST_VER}"

SRC_URI = "${SOURCEFORGE_MIRROR}/boost/${BOOST_P}.tar.bz2"

SRC_URI[md5sum] = "7ef085456c48c49a7fe8237f07e5f674"
SRC_URI[sha256sum] = "bce80293052e2d6230f1bec9b7524b33412e4fb26e9723460a0f362ac15b7acb"

S = "${WORKDIR}/${BOOST_P}"
