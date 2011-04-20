require libmatchbox.inc

PV = "1.9+git${SRCPV}"
PR = "r0"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://configure_fixes.patch;patch=1 \
	   file://check.m4 \
	   file://16bppfixes.patch;patch=1 \
	   file://matchbox-start-fix.patch;patch=1"

S = "${WORKDIR}/git"

do_configure_prepend () {
        cp ${WORKDIR}/check.m4 ${S}/
}
