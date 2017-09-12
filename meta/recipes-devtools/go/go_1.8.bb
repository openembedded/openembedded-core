require go-${PV}.inc
require go-target.inc
TUNE_CCARGS_remove = "-march=mips32r2"
SECURITY_PIE_CFLAGS_remove = "-fPIE -pie"
