SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.55.tar.bz2/0672f648a9ee8607a2df65835c54f5e5/initscripts-9.55.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "0672f648a9ee8607a2df65835c54f5e5"
SRC_URI[sha256sum] = "546d4403a4efa3c4fa6de06a195013d4e64738799c2c779e56d900e7b232a9fa"

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
