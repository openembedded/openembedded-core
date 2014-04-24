require libgcc-common.inc

DEPENDS = "virtual/${TARGET_PREFIX}gcc-initial"

STAGINGCC = "gcc-cross-initial"
STAGINGCC_class-nativesdk = "gcc-crosssdk-initial"
PATH_prepend = "${STAGING_BINDIR_TOOLCHAIN}.${STAGINGCC}:"

PACKAGES = ""

EXTRA_OECONF += "--disable-shared"

LIBGCCBUILDTREENAME = "gcc-build-internal-initial-"

do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_TCBOOTSTRAP}/"

