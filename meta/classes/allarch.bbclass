#
# This class is used for architecture independent recipes/data files (usally scripts)
#

PACKAGE_ARCH = "all"

# No need for virtual/libc or a cross compiler
INHIBIT_DEFAULT_DEPS = "1"

# Set these to a common set of values, we shouldn't be using them other that for WORKDIR directory
# naming anyway
TARGET_ARCH = "all"
TARGET_OS = "linux"
TARGET_CC_ARCH = "none"
PACKAGE_EXTRA_ARCHS = ""
