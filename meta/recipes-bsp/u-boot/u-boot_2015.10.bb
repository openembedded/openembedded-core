require u-boot.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2015.10"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "5ec0003b19cbdf06ccd6941237cbc0d1c3468e2d"

SRC_URI += " \
    file://0001-powerpc-cpu-u-boot-.lds-remove-_GLOBAL_OFFSET_TABLE_.patch \
    file://0002-image.c-Fix-non-Android-booting-with-ramdisk-and-or-.patch \
    file://0003-common-board_f-enable-setup_board_part1-for-MIPS.patch \
    file://0004-MIPS-bootm-rework-and-fix-broken-bootm-code.patch \
    file://0005-MIPS-bootm-use-CONFIG_IS_ENABLED-everywhere.patch \
    file://0006-Replace-extern-inline-with-static-inline.patch \
"

PV = "v2015.10+git${SRCPV}"
