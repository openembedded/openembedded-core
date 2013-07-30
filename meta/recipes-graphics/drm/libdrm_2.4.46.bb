require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "9cba217fd3daa10b1d052ec60d3aa7d5"
SRC_URI[sha256sum] = "33cf320dad4e8060768714792e12643ddf6756a719d262ba7d60b39c2b2650f1"

