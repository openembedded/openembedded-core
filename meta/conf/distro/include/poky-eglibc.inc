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


# eglibc is configured for OLDEST_KERNEL kernel version, so we set minimal
# sane versions there

OLDEST_KERNEL ?= "2.4.0"

# ARM OABI is fine with 2.4.0, EABI is not, so we override on "-gnueabi"
# TARGET_OS here, not on architecture which is 'arm' in both cases
OLDEST_KERNEL_linux-gnueabi ?= "2.6.16"
OLDEST_KERNEL_sparc64 ?= "2.4.21"
OLDEST_KERNEL_ppc64 ?= "2.4.21"

