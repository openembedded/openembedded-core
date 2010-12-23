DESCRIPTION = "script to manage module configuration files."
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PACKAGE_ARCH = "all"
RDEPENDS_${PN} = "${@base_contains("MACHINE_FEATURES", "kernel26",  "module-init-tools-depmod","modutils-depmod",d)} "
PR = "r8"

SRC_URI = "file://update-modules \
           file://COPYING.GPL"

pkg_postinst() {
if [ "x$D" != "x" ]; then
	exit 1
fi
update-modules
}

do_install() {
	install -d ${D}${sbindir}
	install ${WORKDIR}/update-modules ${D}${sbindir}
}

# The Unslung distro uses a 2.4 kernel for a machine (the NSLU2) which
# supports both 2.4 and 2.6 kernels.  Rather than forcing OE to have
# to deal with that unique legacy corner case, we just nullify the
# RDEPENDS_${PN} here and handle it in the Unslung image recipe. I know this
# is ugly.  Please don't remove it unless you first make the RDEPENDS
# line at the top of this file understand that a machine can be used
# in both a 2.4 kernel distro and a 2.6 kernel distro.  Really, it's
# not worth the effort to do that, so just overlook the next line.
RDEPENDS_unslung = ""
