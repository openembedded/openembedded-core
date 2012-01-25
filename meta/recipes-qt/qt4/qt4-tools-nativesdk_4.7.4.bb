require qt4-tools-nativesdk.inc

PR = "${INC_PR}.0"

SRC_URI += "file://blacklist-diginotar-certs.diff \
            file://fix-qtbug-20925.patch \
            file://compile.test-lflags.patch "

SRC_URI[md5sum] = "9831cf1dfa8d0689a06c2c54c5c65aaf"
SRC_URI[sha256sum] = "97195ebce8a46f9929fb971d9ae58326d011c4d54425389e6e936514f540221e"
