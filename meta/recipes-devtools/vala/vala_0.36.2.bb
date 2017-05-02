require ${BPN}.inc

SRC_URI += " file://0001-git-version-gen-don-t-append-dirty-if-we-re-not-in-g.patch \
             file://0001-vapigen.m4-use-PKG_CONFIG_SYSROOT_DIR.patch \
"

SRC_URI[md5sum] = "481774eb4f0f5aa6987e5ea30a0aea31"
SRC_URI[sha256sum] = "863dbfb399d59289dfc34379c0cd34d94e505a49787497550588810310cdf689"
