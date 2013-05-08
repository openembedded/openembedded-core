require gnutls.inc

PR = "${INC_PR}.4"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
            file://avoid_AM_PROG_MKDIR_P_warning_error_with_automake_1.12.patch \
            ${@['', 'file://fix-gettext-version.patch'][bb.data.inherits_class('native', d) or (not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1))]} \
           "

SRC_URI[md5sum] = "f3c1d34bd5f113395c4be0d5dfc2b7fe"
SRC_URI[sha256sum] = "dfa67a7e40727eb0913e75f3c44911d5d8cd58d1ead5acfe73dd933fc0d17ed2"
