SECTION = "base"
PR = "r0"
require fakechroot_${PV}.bb
inherit native

S = "${WORKDIR}/fakechroot-${PV}"

EXTRA_OECONF = " --program-prefix="

do_stage_append () {
    oe_libinstall -so libfakechroot ${STAGING_LIBDIR}/libfakechroot/
}
