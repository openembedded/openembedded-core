TOOLCHAIN_TARGET_SDKTASK ?= "task-poky-standalone-gmae-sdk-target task-poky-standalone-gmae-sdk-target-dbg"
TOOLCHAIN_TARGET_TASK = "${TOOLCHAIN_TARGET_SDKTASK}"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-sdk-${DISTRO_VERSION}"
require meta-toolchain.bb
