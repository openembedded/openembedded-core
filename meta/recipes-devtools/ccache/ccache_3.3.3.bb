require ccache.inc

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=938c02728101936f2a74a67d1d82ee25"

SRC_URI[md5sum] = "ea1f95303749b9ac136c617d1b333eef"
SRC_URI[sha256sum] = "87a399a2267cfac3f36411fbc12ff8959f408cffd050ad15fe423df88e977e8f"

SRC_URI += " \
            file://0002-dev.mk.in-fix-file-name-too-long.patch \
            file://Revert-Create-man-page-in-the-make-install-from-git-.patch \
"
