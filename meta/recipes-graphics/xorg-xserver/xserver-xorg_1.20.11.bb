require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
           file://pkgconfig.patch \
           file://0001-test-xtest-Initialize-array-with-braces.patch \
           file://sdksyms-no-build-path.patch \
           file://0001-drmmode_display.c-add-missing-mi.h-include.patch \
           "
SRC_URI[md5sum] = "a22ccd74a5eee721cad6805e489c7144"
SRC_URI[sha256sum] = "914c796e3ffabe1af48071d40ccc85e92117c97a9082ed1df29e4d64e3c34c49"

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
