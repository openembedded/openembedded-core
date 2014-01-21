SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-${PV}.tar.bz2/2453811ec27a781a77f309f356663d9e/initscripts-${PV}.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "2453811ec27a781a77f309f356663d9e"
SRC_URI[sha256sum] = "0b3bb828a5d3a3f210b8e606c0416813ba0d021a859658af14810865d2ee8867"

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
	install -m 0644 ${S}/rc.d/init.d/functions ${D}${sysconfdir}/init.d/functions
}
