require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
	     file://disable-graphviz.patch \
	     file://0001-Disable-valadoc.patch \
"

SRC_URI[md5sum] = "f34cb465a164ba7f1f2e7fea31f36d13"
SRC_URI[sha256sum] = "b528906d080ac5b6043285a82c194c988e495c70da71ae19c1fc7811509f4f1e"
