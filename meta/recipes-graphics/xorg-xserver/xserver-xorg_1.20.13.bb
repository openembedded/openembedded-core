require xserver-xorg.inc

SRC_URI += "file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
           file://pkgconfig.patch \
           file://0001-test-xtest-Initialize-array-with-braces.patch \
           file://sdksyms-no-build-path.patch \
           file://0001-drmmode_display.c-add-missing-mi.h-include.patch \
           "
SRC_URI[md5sum] = "1724c6efe8599fb0dc6c69c5f268de71"
SRC_URI[sha256sum] = "26f801f4d92216995f389873cf3b4e90069cf63e94bc5dd09ebbf7fd7e1ddcc2"

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
