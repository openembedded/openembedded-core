DESCRIPTION = "display disk usage and limits"
SECTION = "base"
HOMEPAGE = "http://sourceforge.net/projects/linuxquota/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=18136&atid=118136"
LICENSE = "BSD & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://quota.c;beginline=1;endline=33;md5=331c7d77744bfe0ad24027f0651028ec \
                    file://rquota_server.c;beginline=1;endline=20;md5=91cf52a9aa19e13c2797bd2bf785d7c4 \
                    file://svc_socket.c;beginline=1;endline=17;md5=24d5a8792da45910786eeac750be8ceb"
PR = "r0"

SRC_URI = "http://downloads.sourceforge.net/project/linuxquota/quota-tools/${PV}/quota-${PV}.tar.gz"

S = ${WORKDIR}/quota-tools

inherit autotools

do_install() {
	oe_runmake prefix=${D}${prefix} install
}
