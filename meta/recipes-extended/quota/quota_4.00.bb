DESCRIPTION = "display disk usage and limits"
SECTION = "base"
HOMEPAGE = "http://sourceforge.net/projects/linuxquota/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=18136&atid=118136"
LICENSE = "BSD & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://quota.c;beginline=1;endline=33;md5=331c7d77744bfe0ad24027f0651028ec \
                    file://rquota_server.c;beginline=1;endline=20;md5=fe7e0d7e11c6f820f8fa62a5af71230f \
                    file://svc_socket.c;beginline=1;endline=17;md5=24d5a8792da45910786eeac750be8ceb"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/linuxquota/quota-tools/${PV}/quota-${PV}.tar.gz"

SRC_URI[md5sum] = "975f587ff761a60ac25dbe6c00865925"
SRC_URI[sha256sum] = "eb2b045f58b98299edc568b8607b95b10735b4a131fa5196f41c89b51fb409ba"

S = "${WORKDIR}/quota-tools"

inherit autotools

EXTRA_OEMAKE += 'STRIP=""'

do_install() {
	oe_runmake prefix=${D}${prefix} install
}
