# NOTE: If you want to optimize to sh4a, conf/machine/include/tune-sh4a.inc.
# But it is not compatible for sh4.
# The binary optimized by m4a doesn't operate on sh4. It works on sh4a only.

TARGET_CC_ARCH = "-ml -m4"
FEED_ARCH = "sh4"
BASE_PACKAGE_ARCH = "sh4"
