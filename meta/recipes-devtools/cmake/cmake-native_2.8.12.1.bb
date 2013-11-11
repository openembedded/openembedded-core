require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "file://cmlibarchive-disable-ext2fs.patch"

SRC_URI[md5sum] = "9d38cd4e2c94c3cea97d0e2924814acc"
SRC_URI[sha256sum] = "fa28c12791d64c36ba6b6cb062a4b4bd4223053f6b9ea501b1bdbdf4d5df3a67"
