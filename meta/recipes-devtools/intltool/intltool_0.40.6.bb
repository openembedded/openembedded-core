require intltool.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r4"

SRC_URI_append = " file://intltool-nowarn-0.40.0.patch \
                   ${NATIVEPATCHES} \
                 "

NATIVEPATCHES = ""
NATIVEPATCHES_virtclass-native = "file://use-nativeperl.patch"

SRC_URI[md5sum] = "69bc0353323112f42ad4f9cf351bc3e5"
SRC_URI[sha256sum] = "4d1e5f8561f09c958e303d4faa885079a5e173a61d28437d0013ff5efc9e3b64"
