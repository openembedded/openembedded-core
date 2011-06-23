#
# uclibc specific configuration
#

TARGET_OS = "linux-uclibc"
TARGET_OS_arm = "linux-uclibceabi"
TARGET_OS_armeb = "linux-uclibceabi"

# Add uclibc overrides to the overrides.
OVERRIDES =. "libc-uclibc:"

PREFERRED_PROVIDER_virtual/libc = "uclibc"
PREFERRED_PROVIDER_virtual/libiconv ?= "libiconv"
PREFERRED_PROVIDER_virtual/libintl ?= "gettext"

USE_NLS ?= "no"

CXXFLAGS += "-fvisibility-inlines-hidden"

IMAGE_LINGUAS = ""

LIBC_DEPENDENCIES = "\
    uclibc \
    uclibc-dbg \
    uclibc-dev \
    uclibc-thread-db \
    "
