require xserver-xorg.inc

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch \
            file://xorg-CVE-2013-6424.patch \
            file://xshmfence-option.patch \
            file://Fix-subwindow-in-Xi-emulated-events.patch \
            file://xtrans.patch \
           "

SRC_URI[md5sum] = "afd93977235584a9caa7528a737c1b52"
SRC_URI[sha256sum] = "5e0f443238af1078b48f6eea98a382861b59187da221c2cf714d31c1d560b0fb"

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
