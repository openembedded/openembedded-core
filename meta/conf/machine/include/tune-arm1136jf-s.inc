TARGET_CC_ARCH = "-march=armv6j -mtune=arm1136jf-s"
TARGET_CC_ARCH += "${@['', '-mfloat-abi=softfp -mfpu=vfp'][(bb.data.getVar('TARGET_FPU', d, 1) == 'soft') and (bb.data.getVar('CPU_FEATURES', d, 1).find('vfp') != -1)]}"
FEED_ARCH = "armv6"
PACKAGE_ARCH = "armv6"

ARM_INSTRUCTION_SET = "${@['arm', 'thumb'][bb.data.getVar('CPU_FEATURES', d, 1).find('thumb') != 1]}"

require conf/machine/include/tune-thumb.inc
