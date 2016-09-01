require ${BPN}.inc

SRC_URI += "file://0001-Use-proc-self-exe-for-swig-swiglib-on-non-Win32-plat.patch \
            file://0001-configure-use-pkg-config-for-pcre-detection.patch \
           "
SRC_URI[md5sum] = "bb4ab8047159469add7d00910e203124"
SRC_URI[sha256sum] = "2939aae39dec06095462f1b95ce1c958ac80d07b926e48871046d17c0094f44c"

