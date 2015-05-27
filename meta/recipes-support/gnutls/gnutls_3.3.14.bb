require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "7f4465f8c564cf9cb8f5cb38b909f7ca"
SRC_URI[sha256sum] = "0dfa0030faad8909c1e904105198232d6bc0123cae8cf4933b2bac85ee7cec52"
