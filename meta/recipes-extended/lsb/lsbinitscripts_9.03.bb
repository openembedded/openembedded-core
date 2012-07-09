DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"
PR = "r2"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-${PV}.tar.bz2/668fa2762b57ef75436303857847bba3/initscripts-${PV}.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "668fa2762b57ef75436303857847bba3"
SRC_URI[sha256sum] = "d56547a68ce223a7413b2676650b042125f047c8d6d139c5b970e118b3dc958a"
inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "functions"
ALTERNATIVE_LINK_NAME[functions] = "${sysconfdir}/init.d/functions"

do_configure[noexec] = "1" 

do_install(){
	install -d ${D}/etc/init.d/
	install -m 0755 ${S}/rc.d/init.d/functions ${D}/etc/init.d/functions
}
