require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "\
    file://cmlibarchive-disable-ext2fs.patch \
"

SRC_URI[md5sum] = "17c6513483d23590cbce6957ec6d1e66"
SRC_URI[sha256sum] = "8c6574e9afabcb9fc66f463bb1f2f051958d86c85c37fccf067eb1a44a120e5e"

# Disable ccmake since we don't depend on ncurses
CMAKE_EXTRACONF = "\
    -DBUILD_CursesDialog=0 \
    -DENABLE_ACL=0 -DHAVE_ACL_LIBACL_H=0 \
"
