SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.61.tar.bz2/892006be882b41f680109cb7336949d1/initscripts-9.61.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "892006be882b41f680109cb7336949d1"
SRC_URI[sha256sum] = "fb090d3fc48adbc0bb5835090e7eb5f1aa7e33d39537523d017a604527a59653"

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
