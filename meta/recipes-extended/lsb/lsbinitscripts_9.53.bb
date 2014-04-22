SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.53.tar.bz2/6842b4279d67acae40ce14263e241805/initscripts-9.53.tar.bz2 \
           file://functions.patch \
          " 

SRC_URI[md5sum] = "6842b4279d67acae40ce14263e241805"
SRC_URI[sha256sum] = "b4d97e259de34a5afc2b08bdc402201f4d0a0e02b4961f12c02ff461a5b305e1"

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
