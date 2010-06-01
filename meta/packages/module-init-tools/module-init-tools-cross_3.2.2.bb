require module-init-tools.inc
PR = "r4"
inherit cross
PROVIDES += "virtual/${TARGET_PREFIX}depmod virtual/${TARGET_PREFIX}depmod-2.6"

SRC_URI += "file://no-static-binaries.patch;patch=1"

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX}"

do_install_append () {
        mv ${D}${bindir}/${TARGET_PREFIX}depmod ${D}${bindir}/${TARGET_PREFIX}depmod-2.6
}
