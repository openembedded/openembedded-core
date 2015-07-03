SUMMARY = "SysV init scripts which are only used in an LSB image"
SECTION = "base"
LICENSE = "GPLv2"
DEPENDS = "popt glib-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=ebf4e8b49780ab187d51bd26aaa022c6"

S="${WORKDIR}/initscripts-${PV}"
SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/initscripts/initscripts-9.63.tar.bz2/1ae0f15e54a904ac30185548d43daa1c/initscripts-9.63.tar.bz2 \
           file://functions.patch \
           file://0001-functions-avoid-exit-1-which-causes-init-scripts-to-.patch \
          " 

SRC_URI[md5sum] = "1ae0f15e54a904ac30185548d43daa1c"
SRC_URI[sha256sum] = "39474c3a83e2e03077e27bf9418a4296651c421cf0ba7672e67fe2e3567b36e1"

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
