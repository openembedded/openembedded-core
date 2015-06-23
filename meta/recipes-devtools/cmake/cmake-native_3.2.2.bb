require cmake.inc
inherit native

# Using cmake's internal libarchive, so some dependencies are needed
DEPENDS += "bzip2-native zlib-native"

SRC_URI += "\
    file://cmlibarchive-disable-ext2fs.patch \
"

SRC_URI[md5sum] = "2da57308071ea98b10253a87d2419281"
SRC_URI[sha256sum] = "ade94e6e36038774565f2aed8866415443444fb7a362eb0ea5096e40d5407c78"

# Disable ccmake since we don't depend on ncurses
CMAKE_EXTRACONF = "\
    -DBUILD_CursesDialog=0 \
    -DENABLE_ACL=0 -DHAVE_ACL_LIBACL_H=0 \
    -DHAVE_SYS_ACL_H=0 \
"
