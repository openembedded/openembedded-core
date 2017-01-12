require ${BPN}.inc

SRC_URI += "file://0001-Use-proc-self-exe-for-swig-swiglib-on-non-Win32-plat.patch \
            file://0001-configure-use-pkg-config-for-pcre-detection.patch \
           "
SRC_URI[md5sum] = "13732eb0f1ab2123d180db8425c1edea"
SRC_URI[sha256sum] = "d9031d531d7418829a54d0d51c4ed9007016b213657ec70be44031951810566e"

