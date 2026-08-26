require u-boot-common.inc
require u-boot.inc

DEPENDS += "bc-native dtc-native python3-pyelftools-native"

SRC_URI += "file://CVE-2024-57254.patch \
            file://CVE-2024-57255.patch \
            file://CVE-2024-57256.patch \
            file://CVE-2024-57257.patch \
            file://CVE-2024-57258-1.patch \
            file://CVE-2024-57258-2.patch \
            file://CVE-2024-57258-3.patch \
            file://CVE-2024-57259.patch \
            file://CVE-2024-42040.patch \
            file://CVE-2026-46728.patch \
"
