require apt-native.inc

SRC_URI += "file://noconfigure.patch \
            file://no-curl.patch"

SRC_URI[md5sum] = "72b3283acd9b99868da5545f0499b0da"
SRC_URI[sha256sum] = "770cb94d7f4c922c2a1516f2b5ec852d3ad668a8c9c3713ac2528c861b7fa79a"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
