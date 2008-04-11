DEPENDS = "flex-native"
SRC_URI = "svn://llvm.org/svn/llvm-project/llvm;proto=http;module=trunk"

PV = "0.0+${SRCREV}"

S = "${WORKDIR}/trunk"

inherit autotools native 

EXTRA_OECONF = "--enable-optimized"
EXTRA_OEMAKE = "ENABLE_OPTIMIZED=1"