require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "cb48bb0cf36d329f7321c6cdc0d7ae36"
SRC_URI[sha256sum] = "bc4a0f80a627c3aca6e7ea59d30e50cda118c61e0e3fab367ff1451d6ec8bdbd"
