require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
           "
SRC_URI[md5sum] = "fb84c4d7922c1545da8dda4dcb9487d4"
SRC_URI[sha256sum] = "86994fe7804ee16d2811e366b9bf2f75304f8e470ae0e3716d60ffeedac0e529"

