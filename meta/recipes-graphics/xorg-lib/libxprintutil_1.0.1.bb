require xorg-lib-common.inc
LIC_FILES_CHKSUM = "file://src/xprintutil.c;endline=29;md5=22d5a98494dd356a2c6c9c355886f3af"

SUMMARY = "Xprint: Xprint printer utility library"

DESCRIPTION = "libxprintutil provides utility Xpu APIs allowing client \
applications to access and manipulate information about printer \
capabilities from an Xprint server."

DEPENDS += "libxp libxt"
PR = "r1"
PE = "1"

XORG_PN = "libXprintUtil"

SRC_URI[md5sum] = "22584f1aab1deba253949b562d1f0f45"
SRC_URI[sha256sum] = "72b6ae0420b9601f55be147e8d068f670b951ae73a81423ba25be5875d826e6c"
