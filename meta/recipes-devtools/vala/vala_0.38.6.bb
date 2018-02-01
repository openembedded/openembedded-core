require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "e0e834869f636fde981ba63d46f31c9c"
SRC_URI[sha256sum] = "8c676b8307a12fba3420f861463c7e40b2743b0d6fef91f9516a3441ea25029a"
