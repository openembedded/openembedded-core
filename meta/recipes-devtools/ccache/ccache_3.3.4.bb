require ccache.inc

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=7fe21f9470f2305e95e7d8a632255079"

SRC_URI[md5sum] = "61326f1edac7cd211a7018458dfe2d86"
SRC_URI[sha256sum] = "1348b54e7c35dd2f8d17923389e03c546e599cfbde6459d2f31cf6f1521ec538"

SRC_URI += " \
            file://0002-dev.mk.in-fix-file-name-too-long.patch \
            file://Revert-Create-man-page-in-the-make-install-from-git-.patch \
"
