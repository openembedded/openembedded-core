DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-${PV}.tar.bz2/dd514ee65e4a4610be836bb34c62c0ed/initscripts-${PV}.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "dd514ee65e4a4610be836bb34c62c0ed"
SRC_URI[sha256sum] = "dd8bfa09f41f56c223f90629f31fc16decc3b9df4c72b1e3ee00cfa3f7d9c48c"

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"
ALTERNATIVE_${PN} = "functions"
ALTERNATIVE_LINK_NAME[functions] = "${sysconfdir}/init.d/functions"

# Since we are only taking the patched version of functions, no need to
# configure or compile anything so do not execute these
do_configure[noexec] = "1" 
do_compile[noexec] = "1" 

do_install(){
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${S}/rc.d/init.d/functions ${D}${sysconfdir}/init.d/functions
}
