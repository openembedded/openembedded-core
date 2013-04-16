require xserver-xorg.inc

# Misc build failure for master HEAD
SRC_URI += "file://crosscompile.patch \
            file://fix_open_max_preprocessor_error.patch \
            file://mips64-compiler.patch \
            file://aarch64.patch \
            file://fix_compilation_when_not_using_xinerama.patch \
            file://0001-dixstruct.h-fix-segfaults-char-is-unsigned-for-ARM-a.patch \
           "

SRC_URI[md5sum] = "86110278b784e279381b7f6f2295c508"
SRC_URI[sha256sum] = "1f5107573252c26439fdd165481765a2c0964e02a2e9fab36e02414d08f30630"

PR = "${INC_PR}.1"

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
