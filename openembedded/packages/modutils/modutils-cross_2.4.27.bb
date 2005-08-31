SECTION = "base"
include modutils_${PV}.bb
PR = "r6"
inherit cross
S = "${WORKDIR}/modutils-${PV}"
DEPENDS = ""
PACKAGES = ""
PROVIDES += "virtual/${TARGET_PREFIX}depmod virtual/${TARGET_PREFIX}depmod-2.4"
DEFAULT_PREFERENCE = "1"

SRC_URI +=  "file://modutils-cross/module.h.diff;patch=1"

sbindir = "${prefix}/bin"

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX}"

CFLAGS_prepend_mipsel = "-D__MIPSEL__"
CFLAGS_prepend_mipseb = "-D__MIPSEB__"

do_stage () {
        oe_runmake install
}

do_install () {
        :
}
