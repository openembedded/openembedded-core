DESCRIPTION = "SysV init scripts which only is used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.47.tar.bz2/93a50efd8d262f3bceb0d15c8b8b02ab/initscripts-9.47.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "93a50efd8d262f3bceb0d15c8b8b02ab"
SRC_URI[sha256sum] = "6380e00f677b55ee73eee8a21f68bf717ab614cef09cb4a379aca0d7c717f57e"

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
