require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "e7d05683bcac0306fc8d7c875eb3fef1"
SRC_URI[sha256sum] = "75a3dc2de36873d8ceab168b2fde1b2c378a1f7104a3b3391ba3acf579c674b3"
