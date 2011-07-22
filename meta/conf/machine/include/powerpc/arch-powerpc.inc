TUNE_ARCH = "powerpc"

ABIEXTENSION = "${@['','spe'][bb.data.getVar('TARGET_FPU',d,1) in ['ppc-efd', 'ppc-efs']]}"
