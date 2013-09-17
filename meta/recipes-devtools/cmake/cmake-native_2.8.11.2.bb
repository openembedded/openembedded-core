require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "file://cmlibarchive-disable-ext2fs.patch"

SRC_URI[md5sum] = "6f5d7b8e7534a5d9e1a7664ba63cf882"
SRC_URI[sha256sum] = "b32acb483afdd14339941c6e4ec25f633d916a7a472653a0b00838771a6c0562"
