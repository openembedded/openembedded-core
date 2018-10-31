require xserver-xorg.inc

SRC_URI += "file://musl-arm-inb-outb.patch \
            file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
            file://pkgconfig.patch \
            "
SRC_URI[md5sum] = "8ee29e8b24cef6b3cfa747ec01b9155a"
SRC_URI[sha256sum] = "1b3ce466c12cacbe2252b3ad5b0ed561972eef9d09e75900d65fb1e21f9201de"

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
