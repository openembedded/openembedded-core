TOOLCHAIN_TARGET_GMAETASK ?= "task-core-standalone-gmae-sdk-target task-core-standalone-gmae-sdk-target-dbg"
TOOLCHAIN_TARGET_TASK = "${TOOLCHAIN_TARGET_GMAETASK}"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-gmae-${DISTRO_VERSION}"
PROVIDES = "meta-toolchain-sdk"
require meta-toolchain.bb

TOOLCHAIN_NEED_CONFIGSITE_CACHE += "zlib"
