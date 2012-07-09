require gnutls.inc

PR = "${INC_PR}.3"

SRC_URI += "file://gnutls-openssl.patch \
            file://correct_rpl_gettimeofday_signature.patch \
            file://configure-fix.patch \
            file://gnutls_fix_for_automake_1.12.1.patch \
            file://avoid_AM_PROG_MKDIR_P_warning_error_with_automake_1.12.patch \
            ${@['', 'file://fix-gettext-version.patch'][bb.data.inherits_class('native', d) or (not ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1))]} \
            file://remove-gets.patch \
           "

SRC_URI[md5sum] = "f1dea97da5d4dcdbc675720c9aad9ee3"
SRC_URI[sha256sum] = "4884eafcc8383ed23209199bbc72ad04f4eb94955a50a594125ff34c6889c564"
