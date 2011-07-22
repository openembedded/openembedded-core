TUNE_ARCH = "sh4"

# NOTE: If you want to optimize to sh4a, conf/machine/include/tune-sh4a.inc.
# But it is not compatible for sh4.
# The binary optimized by m4a doesn't operate on sh4. It works on sh4a only.

TUNE_CCARGS = "-ml -m4"
TUNE_PKGARCH = "sh4"
