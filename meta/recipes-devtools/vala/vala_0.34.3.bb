require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
"

SRC_URI[md5sum] = "71181dd25d06b0bedd378dc8fa7d6310"
SRC_URI[sha256sum] = "f0fad71aca03cdeadf749ca47f56296a4ddd1a25f4e2f09f0ff9e1e3afbcac3f"
