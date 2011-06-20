DESCRIPTION = "display disk usage and limits"
SECTION = "base"
HOMEPAGE = "http://sourceforge.net/projects/linuxquota/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=18136&atid=118136"
LICENSE = "BSD & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://quota.c;beginline=1;endline=33;md5=331c7d77744bfe0ad24027f0651028ec \
                    file://rquota_server.c;beginline=1;endline=20;md5=d509328bb71c3438b9c737774b4132a2 \
                    file://svc_socket.c;beginline=1;endline=17;md5=24d5a8792da45910786eeac750be8ceb"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/linuxquota/quota-tools/${PV}/quota-${PV}.tar.gz"

SRC_URI[md5sum] = "ef9d66e8a968ecffd2d9df648fa8ada2"
SRC_URI[sha256sum] = "181a9b90b10bbffaaf9a18e7fef96a5752ab20f7b72d81c472166ab32e415994"

S = ${WORKDIR}/quota-tools

inherit autotools

EXTRA_OEMAKE += 'STRIP=""'

do_install() {
	oe_runmake prefix=${D}${prefix} install
}
