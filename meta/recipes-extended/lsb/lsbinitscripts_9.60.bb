SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.60.tar.bz2/5c9cd83e4e04257fef3ffd8e549b7f81/initscripts-9.60.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "5c9cd83e4e04257fef3ffd8e549b7f81"
SRC_URI[sha256sum] = "254508884ceeef22e24f4e8b051bb51958ae8252bd7b02cd2b7bcce3fb453dc2"

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
	sed -i 's,${base_bindir}/mountpoint,${bindir}/mountpoint,g' ${D}${sysconfdir}/init.d/functions
}
