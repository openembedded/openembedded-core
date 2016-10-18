require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
"

SRC_URI[md5sum] = "4c04e73025127e3ae43b968149c96329"
SRC_URI[sha256sum] = "f5ccfcfc460a0c6797bfbd7e739042bf5988a0f44d82278dbe1880c0e5f29299"
