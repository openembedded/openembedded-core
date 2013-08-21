require libx11.inc
inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI += "file://disable_tests.patch \
           "

SRC_URI[md5sum] = "bc5fc459ec25a50c5a7e27035b89e579"
SRC_URI[sha256sum] = "10a54fc16b58b4f5a5eed4d080c357a82fd2f42d09af625c1f5df50650701892"
