require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "e7556cec73c8b34fd2ff0b591e24e44c"
SRC_URI[sha256sum] = "c1be9e4b30295d7b5f96fa332c6a908e6fa2254377b67811301fca92eb882e5a"
