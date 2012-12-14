DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"
PR = "r3"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.43.tar.bz2/a225c9b7b0786e395b38a4d919e7d72f/initscripts-9.43.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "a225c9b7b0786e395b38a4d919e7d72f"
SRC_URI[sha256sum] = "9503c71e4f646f3fc5e7a10d5f4e6f5aea9275249fea596879b2513555d53867"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "functions"
ALTERNATIVE_LINK_NAME[functions] = "${sysconfdir}/init.d/functions"

do_configure[noexec] = "1" 

do_install(){
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${S}/rc.d/init.d/functions ${D}${sysconfdir}/init.d/functions
}
