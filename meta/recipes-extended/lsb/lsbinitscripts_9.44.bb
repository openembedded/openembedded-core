DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.44.tar.bz2/2a59d3bd214b4dc6238046490d536e42/initscripts-9.44.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "2a59d3bd214b4dc6238046490d536e42"
SRC_URI[sha256sum] = "59aed26c98981dff2e94b3ae1a9c57597399653fb592669e16dee3ab090b576e"

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
