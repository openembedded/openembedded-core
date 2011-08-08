require module-init-tools.inc
PR = "r0"
inherit cross
PROVIDES += "virtual/${TARGET_PREFIX}depmod"
RDEPENDS_${PN} = ""

SRC_URI[md5sum] = "bc44832c6e41707b8447e2847d2019f5"
SRC_URI[sha256sum] = "e1f2cdcae64a8effc25e545a5e0bdaf312f816ebbcd0916e4e87450755fab64b"

EXTRA_OECONF_append = " --program-prefix=${TARGET_PREFIX} --disable-static-utils"
