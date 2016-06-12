LICENSE = "GPLv3 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://tests/COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://glob/COPYING.LIB;md5=4a770b67e6be0f60da244beb2de0fce4"
require make.inc

EXTRA_OECONF += "--without-guile"

SRC_URI[md5sum] = "85ad14d08766201ffe71efa866f4fb91"
SRC_URI[sha256sum] = "4e5ce3b62fe5d75ff8db92b7f6df91e476d10c3aceebf1639796dc5bfece655f"

BBCLASSEXTEND = "native nativesdk"
