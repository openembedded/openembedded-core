require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "4da148b5a0048aaac4961e2d9ba95798"
SRC_URI[sha256sum] = "70ef9c9f95822d363036c6e6b5479750e5b7fc34f50e750c3464a98ec65a9ab8"
