#
# eglibc specific configuration
#

TARGET_OS = "linux"
TARGET_OS_arm = "linux-gnueabi"
TARGET_OS_armeb = "linux-gnueabi"
TARGET_OS_powerpc = "linux${@['','-gnuspe'][bb.data.getVar('BASE_PACKAGE_ARCH',d,1) in ['ppce500', 'ppce500v2']]}"

# Add glibc overrides to the overrides for eglibc.
OVERRIDES .= ":libc-glibc"

PREFERRED_PROVIDER_virtual/libiconv ?= "eglibc"
PREFERRED_PROVIDER_virtual/libiconv-nativesdk ?= "eglibc-nativesdk"
PREFERRED_PROVIDER_virtual/libintl ?= "eglibc"
PREFERRED_PROVIDER_virtual/libc ?= "eglibc"
PREFERRED_PROVIDER_virtual/libc-nativesdk ?= "eglibc-nativesdk"

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
    		     locale-base-en-us \
    		     locale-base-en-gb "
