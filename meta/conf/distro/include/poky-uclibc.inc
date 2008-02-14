#
# uclibc specific configuration
#

PREFERRED_PROVIDER_virtual/libc = "uclibc"
PREFERRED_PROVIDER_virtual/libiconv ?= "libiconv"
PREFERRED_PROVIDER_virtual/libintl ?= "gettext"

USE_NLS ?= "no"
USE_NLS_glib-2.0 = "yes"

TARGET_OS = "${UCLIBCTARGETOS}"

CXXFLAGS += "-fvisibility-inlines-hidden"

IMAGE_LINGUAS = ""

DEPLOY_DIR = "${TMPDIR}/deploy-uclibc"
DEPLOY_DIR_IMAGE = "${TMPDIR}/deploy/images"
