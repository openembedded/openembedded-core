require libx11.inc

DESCRIPTION += " Support for XCMS and XLOCALE is disabled in \
this version."

SRC_URI += "file://X18NCMSstubs.diff \
            file://fix-disable-xlocale.diff \
            file://fix-utf8-wrong-define.patch \
           "

RPROVIDES_${PN}-dev = "libx11-dev"
RPROVIDES_${PN}-locale = "libx11-locale"

SRC_URI[md5sum] = "bc5fc459ec25a50c5a7e27035b89e579"
SRC_URI[sha256sum] = "10a54fc16b58b4f5a5eed4d080c357a82fd2f42d09af625c1f5df50650701892"

EXTRA_OECONF += "--disable-xlocale"

PACKAGECONFIG ??= ""
