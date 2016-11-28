require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "7a38b23757aae009c3eb5bb12fb0afda"
SRC_URI[sha256sum] = "6338b715bf31c758606ffa489c7f87ee1beab947114fbd2ffefd73170a8c6b9a"

