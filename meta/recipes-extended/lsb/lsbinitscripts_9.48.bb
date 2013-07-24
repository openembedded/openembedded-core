DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.48.tar.bz2/7dfab81a5a8d3f0dea5ba55e391c26f3/initscripts-9.48.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "7dfab81a5a8d3f0dea5ba55e391c26f3"
SRC_URI[sha256sum] = "5f786121269383d81117532877f6488b48c52a544e0013528dc52c8924f70455"

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
