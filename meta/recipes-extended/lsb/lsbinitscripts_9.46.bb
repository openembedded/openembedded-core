DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.46.tar.bz2/f0755d9f1e0a8ae470c22798695c3b39/initscripts-9.46.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "f0755d9f1e0a8ae470c22798695c3b39"
SRC_URI[sha256sum] = "f1b9003826d6c524c6b144e554ec38e788531270b8df252218708da11c9e4973"

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
