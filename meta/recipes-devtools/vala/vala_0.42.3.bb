require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "6fdd6fadbef27e3dd05086d6a3220556"
SRC_URI[sha256sum] = "5fc73dd1e683dc7391bb96d02b2f671aa8289411a48611a05265629e0048587d"
