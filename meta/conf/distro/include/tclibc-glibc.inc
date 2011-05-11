#
# glibc specific configuration
#

TARGET_OS = "linux"
TARGET_OS_arm = "linux-gnueabi"
TARGET_OS_armeb = "linux-gnueabi"
TARGET_OS_powerpc = "linux${@['','-gnuspe'][bb.data.getVar('BASE_PACKAGE_ARCH',d,1) in ['ppce500', 'ppce500v2']]}"

# Add glibc to the overrides.
OVERRIDES =. "libc-glibc:"

PREFERRED_PROVIDER_virtual/libiconv ?= "glibc"
PREFERRED_PROVIDER_virtual/libiconv-nativesdk ?= "glibc-nativesdk"
PREFERRED_PROVIDER_virtual/libintl ?= "glibc"
PREFERRED_PROVIDER_virtual/libc ?= "glibc"
PREFERRED_PROVIDER_virtual/libc-nativesdk ?= "glibc-nativesdk"

CXXFLAGS += "-fvisibility-inlines-hidden"

LIBC_DEPENDENCIES = "\
    libsegfault \
    glibc \
    glibc-dbg \
    glibc-dev \
    glibc-utils \
    glibc-thread-db \
    glibc-localedata-i18n \
    glibc-gconv-ibm850 \
    glibc-gconv-cp1252 \
    glibc-gconv-iso8859-1 \
    glibc-gconv-iso8859-15 \
    locale-base-en-gb \
    "
