SECTION = "x11/base"
LICENSE = "MIT"
SRC_URI = "git://git.moblin.org/projects/libdrm.git/;protocol=git \
           file://poulsbo_libdrm_update.patch;patch=1"
PROVIDES = "drm libdrm"

S = ${WORKDIR}/git

DEPENDS = "libpthread-stubs"

PR = "r2"
PV = "2.3.0+git${SRCREV}"

#PROVIDES = "libdrm"
COMPATIBLE_MACHINE = "menlow"
#DEFAULT_PREFERENCE_menlow = "5"
#PACKAGE_ARCH = "${MACHINE_ARCH}"

LEAD_SONAME = "libdrm.so"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
