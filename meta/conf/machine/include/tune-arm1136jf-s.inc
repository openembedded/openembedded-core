TUNE_ARCH = "arm"

TARGET_CC_ARCH = "-march=armv6j -mtune=arm1136jf-s"
TARGET_CC_ARCH += "${@['', '-mfloat-abi=softfp -mfpu=vfp'][(bb.data.getVar('TARGET_FPU', d, 1) == 'soft') and (bb.data.getVar('CPU_FEATURES', d, 1).find('vfp') != -1)]}"
TUNE_PKGARCH = "armv6"

ARM_INSTRUCTION_SET = "${@['thumb','arm'][bb.data.getVar('CPU_FEATURES', d, 1).find('thumb') == -1]}"
PACKAGE_EXTRA_ARCHS = "arm armv4 armv4t armv5te armv6"

require conf/machine/include/tune-thumb.inc
