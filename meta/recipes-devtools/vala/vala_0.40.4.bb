require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "a690209e202be7aa6234aaa4e48fe3fb"
SRC_URI[sha256sum] = "379354a2a2f7ee5c4d6e0f5e88b0e32620dcd5f51972baf6d90d9f18eb689198"
