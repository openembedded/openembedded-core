require xorg-app-common.inc

SUMMARY = "Print out X-Video extension adaptor information"

DESCRIPTION = "xvinfo prints out the capabilities of any video adaptors \
associated with the display that are accessible through the X-Video \
extension."

LIC_FILES_CHKSUM = "file://COPYING;md5=b664101ad7a1dc758a4c4109bf978e68"
DEPENDS += " libxv"
PE = "1"

SRC_URI[md5sum] = "c88feb501083951a8f47a21aaeb1529d"
SRC_URI[sha256sum] = "60c74aa190bcf1e244f6f1576dc43869018a8ed5ba319703a5c198d3466a3985"
