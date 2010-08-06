#
# eglibc specific configuration
#

# Add glibc overrides to the overrides for eglibc.
OVERRIDES .= ":libc-glibc"

PREFERRED_PROVIDER_virtual/libiconv ?= "eglibc"
PREFERRED_PROVIDER_virtual/libintl ?= "eglibc"
PREFERRED_PROVIDER_virtual/libc ?= "eglibc"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-initial = "eglibc-initial"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "eglibc"

TARGET_OS = "${GLIBCTARGETOS}"

CXXFLAGS += "-fvisibility-inlines-hidden"

LIBC_DEPENDENCIES = "libsegfault \
		     eglibc \
    		     eglibc-dbg \
    		     eglibc-dev \
    		     eglibc-utils \
    		     eglibc-thread-db \
    		     eglibc-localedata-i18n \
    		     eglibc-gconv-ibm850 \
    		     eglibc-gconv-cp1252 \
    		     eglibc-gconv-iso8859-1 \
    		     eglibc-gconv-iso8859-15 \
    		     locale-base-en-gb "
