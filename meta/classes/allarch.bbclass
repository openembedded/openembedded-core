#
# This class is used for architecture independent recipes/data files (usally scripts)
#

PACKAGE_ARCH = "all"

# No need for virtual/libc or a cross compiler
INHIBIT_DEFAULT_DEPS = "1"

# Set these to a common set of values, we shouldn't be using them other that for WORKDIR directory
# naming anyway
TARGET_ARCH = "allarch"
TARGET_OS = "linux"
TARGET_CC_ARCH = "none"
TARGET_LD_ARCH = "none"
TARGET_AS_ARCH = "none"
PACKAGE_EXTRA_ARCHS = ""

# No need to do shared library processing or debug symbol handling
EXCLUDE_FROM_SHLIBS = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
