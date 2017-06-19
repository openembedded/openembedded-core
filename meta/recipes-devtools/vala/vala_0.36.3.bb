require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
"

SRC_URI[md5sum] = "ff093f46e1d2e0b179543ec43cf27d76"
SRC_URI[sha256sum] = "ac8a4ecd01f62d0c5f62ba50b7290d8c5a1edb308eec772a65b8e79be68f061c"
