require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
            file://0001-present-Fix-use-after-free-in-present_create_notifie.patch \
            file://0002-xkb-Make-the-RT_XKBCLIENT-resource-private.patch \
            file://0003-xkb-Free-the-XKB-resource-when-freeing-XkbInterest.patch \
            file://0004-xkb-Prevent-overflow-in-XkbSetCompatMap.patch \
            "
SRC_URI[sha256sum] = "c878d1930d87725d4a5bf498c24f4be8130d5b2646a9fd0f2994deff90116352"

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
