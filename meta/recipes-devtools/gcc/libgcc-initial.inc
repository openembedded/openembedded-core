require libgcc-common.inc

DEPENDS = "virtual/${TARGET_PREFIX}gcc-initial"

LICENSE = "GPL-3.0-with-GCC-exception"

STAGINGCC = "gcc-cross-initial-${TARGET_ARCH}"
STAGINGCC_class-nativesdk = "gcc-crosssdk-initial-${SDK_SYS}"
PATH_prepend = "${STAGING_BINDIR_TOOLCHAIN}.${STAGINGCC}:"

PACKAGES = ""

EXTRA_OECONF += "--disable-shared"

LIBGCCBUILDTREENAME = "gcc-build-internal-initial-"

inherit nopackages
