require libx11.inc

DESCRIPTION += " Support for XCMS and XLOCALE is disabled in \
this version."

SRC_URI += "file://X18NCMSstubs.patch \
            file://fix-disable-xlocale.patch \
            file://fix-utf8-wrong-define.patch \
           "

RPROVIDES_${PN}-dev = "libx11-dev"
RPROVIDES_${PN}-locale = "libx11-locale"

SRC_URI[md5sum] = "034fdd6cc5393974d88aec6f5bc96162"
SRC_URI[sha256sum] = "910e9e30efba4ad3672ca277741c2728aebffa7bc526f04dcfa74df2e52a1348"

EXTRA_OECONF += "--disable-xlocale"

PACKAGECONFIG ??= ""
