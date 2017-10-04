require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "720846116448fc20b0ab3c0921e19798"
SRC_URI[sha256sum] = "20d5d9c4fbd17877969dbce27e6428da67138e116b1717cc07b5b75fd6ab78a7"
