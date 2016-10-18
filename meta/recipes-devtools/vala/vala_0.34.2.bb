require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
"

SRC_URI[md5sum] = "f9b4a0a10b76b56b0b6e914c506a6828"
SRC_URI[sha256sum] = "765e9c2b429a66db93247940f8588319b43f35c173d057bcae5717a97d765c41"
