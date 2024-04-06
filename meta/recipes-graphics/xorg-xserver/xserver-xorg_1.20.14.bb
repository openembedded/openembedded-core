require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
           file://pkgconfig.patch \
           file://0001-test-xtest-Initialize-array-with-braces.patch \
           file://sdksyms-no-build-path.patch \
           file://0001-drmmode_display.c-add-missing-mi.h-include.patch \
           file://CVE-2022-3550.patch \
           file://CVE-2022-3551.patch \
           file://CVE-2022-3553.patch \
           file://CVE-2022-4283.patch \
           file://CVE-2022-46340.patch \
           file://CVE-2022-46341.patch \
           file://CVE-2022-46342.patch \
           file://CVE-2022-46343.patch \
           file://CVE-2022-46344.patch \
           file://CVE-2023-0494.patch \
           file://CVE-2023-1393.patch \
           file://CVE-2023-5367.patch \
           file://CVE-2023-5380.patch \
           file://CVE-2023-6377.patch \
           file://CVE-2023-6478.patch \
           file://CVE-2023-6816.patch \
           file://CVE-2024-0229-1.patch \
           file://CVE-2024-0229-2.patch \
           file://CVE-2024-0229-3.patch \
           file://CVE-2024-0229-4.patch \
           file://CVE-2024-21885.patch \
           file://CVE-2024-21886-1.patch \
           file://CVE-2024-21886-2.patch \
           file://CVE-2024-0408.patch \
           file://CVE-2024-0409.patch \
           file://CVE-2024-31081.patch \
           file://CVE-2024-31080.patch \
"
SRC_URI[md5sum] = "453fc86aac8c629b3a5b77e8dcca30bf"
SRC_URI[sha256sum] = "54b199c9280ff8bf0f73a54a759645bd0eeeda7255d1c99310d5b7595f3ac066"

CFLAGS += "-fcommon"

# These extensions are now integrated into the server, so declare the migration
# path for in-place upgrades.

RREPLACES_${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RPROVIDES_${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RCONFLICTS_${PN} = "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
