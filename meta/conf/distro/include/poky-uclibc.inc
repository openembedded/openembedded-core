# uclibc:
#fix some iconv issues, needs to be adjusted when doing uclibc builds
PREFERRED_PROVIDER_virtual/libc = "uclibc"
PREFERRED_PROVIDER_virtual/libiconv ?= "libiconv"
PREFERRED_PROVIDER_virtual/libintl ?= "gettext"

USE_NLS ?= "no"
USE_NLS_glib-2.0 = "yes"

TARGET_OS = "${UCLIBCTARGETOS}"

CXXFLAGS += "-fvisibility-inlines-hidden"

IMAGE_LINGUAS = ""
