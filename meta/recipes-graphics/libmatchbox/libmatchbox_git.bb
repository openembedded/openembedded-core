require libmatchbox.inc

SRCREV = "d9dd0ac810de4f0b93cd813ce14aee34c722c2cf"
PV = "1.9+git${SRCPV}"
PR = "r0"
DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://configure_fixes.patch \
	   file://check.m4"

S = "${WORKDIR}/git"

do_configure_prepend () {
        cp ${WORKDIR}/check.m4 ${S}/
}
