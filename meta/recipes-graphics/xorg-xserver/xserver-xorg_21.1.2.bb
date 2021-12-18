require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
           file://0001-Avoid-duplicate-definitions-of-IOPortBase.patch \
           "
SRC_URI[sha256sum] = "c20bf46a9fe8e74bf4e75430637e58d49a02d806609dc161462bceb1ef7e8db0"

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
