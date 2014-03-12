#
# uclibc specific configuration
#

LIBCEXTENSION = "-uclibc"

# Add uclibc overrides to the overrides.
LIBCOVERRIDE = ":libc-uclibc"
OVERRIDES .= "${LIBCOVERRIDE}"

PREFERRED_PROVIDER_virtual/libc ?= "uclibc"
PREFERRED_PROVIDER_virtual/libiconv ?= "libiconv"
PREFERRED_PROVIDER_virtual/libintl ?= "gettext"
PREFERRED_PROVIDER_virtual/nativesdk-libintl ?= "nativesdk-eglibc"
PREFERRED_PROVIDER_virtual/nativesdk-libiconv ?= "nativesdk-eglibc"

USE_NLS ?= "no"

CXXFLAGS += "-fvisibility-inlines-hidden"

IMAGE_LINGUAS = ""

LIBC_DEPENDENCIES = "\
    uclibc \
    uclibc-dbg \
    uclibc-dev \
    uclibc-thread-db \
    "
