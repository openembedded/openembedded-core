require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "file://cmlibarchive-disable-ext2fs.patch"

SRC_URI[md5sum] = "105bc6d21cc2e9b6aff901e43c53afea"
SRC_URI[sha256sum] = "d885ba10b2406ede59aa31a928df33c9d67fc01433202f7dd586999cfd0e0287"
