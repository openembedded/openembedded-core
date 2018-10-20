require libx11.inc

SRC_URI[md5sum] = "034fdd6cc5393974d88aec6f5bc96162"
SRC_URI[sha256sum] = "910e9e30efba4ad3672ca277741c2728aebffa7bc526f04dcfa74df2e52a1348"

SRC_URI += "file://disable_tests.patch \
            file://Fix-hanging-issue-in-_XReply.patch \
           "

inherit gettext

do_configure_append () {
    sed -i -e "/X11_CFLAGS/d" ${B}/src/util/Makefile
}

BBCLASSEXTEND = "native nativesdk"
