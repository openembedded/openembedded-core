require module-init-tools.inc
PR = "r0"
inherit cross
PROVIDES += "virtual/${TARGET_PREFIX}depmod"

SRC_URI += "file://no-static-binaries.patch"

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX}"
