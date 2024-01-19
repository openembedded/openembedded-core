require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
           file://0001-Avoid-duplicate-definitions-of-IOPortBase.patch \
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
           "
SRC_URI[sha256sum] = "38aadb735650c8024ee25211c190bf8aad844c5f59632761ab1ef4c4d5aeb152"

# These extensions are now integrated into the server, so declare the migration
# path for in-place upgrades.

RREPLACES:${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RPROVIDES:${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RCONFLICTS:${PN} = "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
