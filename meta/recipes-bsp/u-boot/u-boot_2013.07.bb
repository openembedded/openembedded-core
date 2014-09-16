require u-boot.inc

# This revision corresponds to the tag "v2013.07"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "62c175fbb8a0f9a926c88294ea9f7e88eb898f6c"

PV = "v2013.07+git${SRCPV}"

SRC_URI += "file://0001-am335x_evm.h-Add-use-DEFAULT_LINUX_BOOT_ENV-environm.patch"
