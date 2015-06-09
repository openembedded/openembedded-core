require xserver-xorg.inc

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch \
            file://xorg-CVE-2013-6424.patch \
            file://xtrans.patch \
            file://0001-use-__GLIBC__-guard-for-glibc-specific-code.patch \
	    file://0001-sdksyms.sh-Make-sdksyms.sh-work-with-gcc5.patch \
	    file://0001-int10-Fix-error-check-for-pci_device_map_legacy.patch \
           "

SRC_URI[md5sum] = "5986510d59e394a50126a8e2833e79d3"
SRC_URI[sha256sum] = "2bf8e9f6f0a710dec1d2472467bff1f4e247cb6dcd76eb469aafdc8a2d7db2ab"

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
