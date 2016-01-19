require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "a26e6dd8d5ad92016e3f068795b89624"
SRC_URI[sha256sum] = "e07c05dea525c6bf0dd8017fc5b89d886954f04fedf457ecd1ce488ac3b86ab7"
