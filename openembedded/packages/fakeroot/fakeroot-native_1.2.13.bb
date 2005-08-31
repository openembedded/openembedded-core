SECTION = "base"
PR = "r0"
include fakeroot_${PV}.bb
inherit native

SRC_URI += "file://fix-prefix.patch;patch=1"
S = "${WORKDIR}/fakeroot-${PV}"

EXTRA_OECONF = "--program-prefix="

do_stage_append () {
    oe_libinstall -so libfakeroot ${STAGING_LIBDIR}/libfakeroot/
}
