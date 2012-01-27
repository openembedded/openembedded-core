require genext2fs.inc

PR = "r1"

SRC_URI += "file://fix-nbblocks-cast.patch"

SRC_URI[md5sum] = "b7b6361bcce2cedff1ae437fadafe53b"
SRC_URI[sha256sum] = "404dbbfa7a86a6c3de8225c8da254d026b17fd288e05cec4df2cc7e1f4feecfc"
