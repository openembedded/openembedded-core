require xserver-xorg.inc

SRC_URI += "file://musl-arm-inb-outb.patch \
            file://0001-configure.ac-Fix-check-for-CLOCK_MONOTONIC.patch \
            file://0002-configure.ac-Fix-wayland-scanner-and-protocols-locat.patch \
            "
SRC_URI[md5sum] = "6a4d01e4e5047ab8f556960424ba1fa9"
SRC_URI[sha256sum] = "149a708b50befc2d5a40b98d45ddd2ebe0beec018e6d0c663c43bad6210e4da3"

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
