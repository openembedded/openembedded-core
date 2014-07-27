require libx11.inc
inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI += "file://disable_tests.patch \
            file://nls-always-use-XCOMM-instead-of-for-comments-in-Comp.patch \
            file://libX11-Add-missing-NULL-check.patch \
           "

SRC_URI[md5sum] = "c35d6ad95b06635a524579e88622fdb5"
SRC_URI[sha256sum] = "2aa027e837231d2eeea90f3a4afe19948a6eb4c8b2bec0241eba7dbc8106bd16"
