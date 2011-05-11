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
USE_NLS_glib-2.0 = "yes"

CXXFLAGS += "-fvisibility-inlines-hidden"

IMAGE_LINGUAS = ""

DEPLOY_DIR_IMAGE = "${TMPDIR}/deploy/images"
DEPLOY_DIR_append = "-uclibc"
STAGING_DIR_TARGET_append = "-uclibc"
STAGING_DIR_HOST_append = "-uclibc"
SSTATE_MANIFESTS_append = "-uclibc"

LIBC_DEPENDENCIES = "\
    uclibc \
    uclibc-dbg \
    uclibc-dev \
    uclibc-thread-db \
    "
