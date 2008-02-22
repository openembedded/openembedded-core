TOOLCHAIN_TARGET_TASK ?= "task-poky-standalone-gmae-sdk-target task-poky-standalone-gmae-sdk-target-dbg"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-sdk-${DISTRO_VERSION}"
require meta-toolchain.bb
