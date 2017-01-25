require gnutls.inc

SRC_URI += "file://correct_rpl_gettimeofday_signature.patch \
            file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
            file://0001-Do-not-add-cli-args.h-to-cli-args.stamp-Makefile-tar.patch \
            file://arm_eabi.patch \
           "
SRC_URI[md5sum] = "08ad2c539bc1d91283f610539deef34e"
SRC_URI[sha256sum] = "60cbfc119e6268cfa38d712621daa473298a0c5b129c0842caec4c1ed4d7861a"

