require module-init-tools.inc
PR = "r2"
inherit cross
PROVIDES += "virtual/${TARGET_PREFIX}depmod virtual/${TARGET_PREFIX}depmod-2.6"

SRC_URI += "file://no-static-binaries.patch"

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX}"
