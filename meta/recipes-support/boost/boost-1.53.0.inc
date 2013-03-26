# The Boost web site provides free peer-reviewed portable
# C++ source libraries. The emphasis is on libraries which
# work well with the C++ Standard Library. The libraries are
# intended to be widely useful, and are in regular use by
# thousands of programmers across a broad spectrum of applications.
HOMEPAGE = "http://www.boost.org/"
LICENSE = "BSL-1.0 & MIT & Python-2.0"
LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

BOOST_VER = "${@"_".join(d.getVar("PV",1).split("."))}"
BOOST_MAJ = "${@"_".join(d.getVar("PV",1).split(".")[0:2])}"
BOOST_P = "boost_${BOOST_VER}"

SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${BOOST_P}.tar.bz2"

SRC_URI[md5sum] = "a00d22605d5dbcfb4c9936a9b35bc4c2"
SRC_URI[sha256sum] = "f88a041b01882b0c9c5c05b39603ec8383fb881f772f6f9e6e6fd0e0cddb9196"

S = "${WORKDIR}/${BOOST_P}"
