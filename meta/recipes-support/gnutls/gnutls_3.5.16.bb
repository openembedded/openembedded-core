require gnutls.inc

SRC_URI += "file://0001-configure.ac-fix-sed-command.patch \
            file://use-pkg-config-to-locate-zlib.patch \
            file://arm_eabi.patch \
           "
SRC_URI[md5sum] = "4c39612f1ec3ef7ed79cfb8936fa8143"
SRC_URI[sha256sum] = "0924dec90c37c05f49fec966eba3672dab4d336d879e5c06e06e13325cbfec25"

BBCLASSEXTEND = "native nativesdk"
