DEPENDS = "flex-native"
SRC_URI = "svn://llvm.org/svn/llvm-project/llvm;proto=http;module=trunk \
           file://fix_ldflags_export.patch;patch=1"

PV = "0.0+${SRCREV}"
PR = "r1"

S = "${WORKDIR}/trunk"

inherit autotools native 

EXTRA_OECONF = "--enable-optimized"
EXTRA_OEMAKE = "ENABLE_OPTIMIZED=1"