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
SRC_URI[md5sum] = "93ffac7507dd39a4c6a672ca6976d397"
SRC_URI[sha256sum] = "2bd020665951f38a230d6b6d98630f8c47ca6977d7d86977d356ccf17756fbf3"
