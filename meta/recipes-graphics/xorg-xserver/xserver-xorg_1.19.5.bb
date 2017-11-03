require xserver-xorg.inc

SRC_URI += "file://musl-arm-inb-outb.patch \
            file://0001-configure.ac-Fix-check-for-CLOCK_MONOTONIC.patch \
            file://0002-configure.ac-Fix-wayland-scanner-and-protocols-locat.patch \
            file://0003-modesetting-Fix-16-bit-depth-bpp-mode.patch \
            file://0003-Remove-check-for-useSIGIO-option.patch \
            "
SRC_URI[md5sum] = "4ac6feeae6790436ce9de879ca9a3bf8"
SRC_URI[sha256sum] = "18fffa8eb93d06d2800d06321fc0df4d357684d8d714315a66d8dfa7df251447"

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
