#
# This class is used for architecture independent recipes/data files (usally scripts)
#

BASE_PACKAGE_ARCH = "all"
PACKAGE_ARCH = "all"

# No need for virtual/libc or a cross compiler
INHIBIT_DEFAULT_DEPS = "1"

# Set these to a common set of values, we shouldn't be using them other that for WORKDIR directory
# naming anyway
TARGET_ARCH = "allarch"
TARGET_OS = "linux"
TARGET_CC_ARCH = "none"
PACKAGE_EXTRA_ARCHS = ""
