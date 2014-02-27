SUMMARY = "Tools for monitoring & limiting user disk usage per filesystem"
SECTION = "base"
HOMEPAGE = "http://sourceforge.net/projects/linuxquota/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=18136&atid=118136"
LICENSE = "BSD & GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://quota.c;beginline=1;endline=33;md5=331c7d77744bfe0ad24027f0651028ec \
                    file://rquota_server.c;beginline=1;endline=20;md5=fe7e0d7e11c6f820f8fa62a5af71230f \
                    file://svc_socket.c;beginline=1;endline=17;md5=24d5a8792da45910786eeac750be8ceb"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/linuxquota/quota-tools/${PV}/quota-${PV}.tar.gz \
           file://config-tcpwrappers.patch"

SRC_URI[md5sum] = "5c2c31e321d2e1322ce12d69ae5c66d6"
SRC_URI[sha256sum] = "a36300bbc126b79b745bf937245092808b4585aa3309ef3335d4ab9d873cd206"

S = "${WORKDIR}/quota-tools"

DEPENDS = "gettext-native e2fsprogs"

inherit autotools-brokensep gettext pkgconfig

EXTRA_OEMAKE += 'STRIP=""'

PACKAGECONFIG ??= "tcp-wrappers"
PACKAGECONFIG[tcp-wrappers] = "--with-tcpwrappers,--without-tcpwrappers,tcp-wrappers"

do_install() {
	oe_runmake prefix=${D}${prefix} install
}
