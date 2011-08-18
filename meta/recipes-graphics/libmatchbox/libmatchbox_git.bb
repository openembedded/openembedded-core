require libmatchbox.inc

SRCREV = "c81f8f444b83b187727f046432b186d67a42c732"
PV = "1.9+git${SRCPV}"
PR = "r0"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://configure_fixes.patch \
	   file://check.m4 \
	   file://16bppfixes.patch \
	   file://matchbox-start-fix.patch"

S = "${WORKDIR}/git"

do_configure_prepend () {
        cp ${WORKDIR}/check.m4 ${S}/
}
