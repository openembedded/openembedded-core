require ${BPN}.inc

SRC_URI += "file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
           file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
           "

SRC_URI[md5sum] = "a0a2eb8babb72c1b4e81c4f1b98429df"
SRC_URI[sha256sum] = "1a7847d2599d902c805a58b95b72b69e64c0223c2f6220163998a7ab4b42db1d"
