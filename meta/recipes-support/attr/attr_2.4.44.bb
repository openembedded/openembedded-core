require attr.inc

PR = "r3"

SRC_URI += "file://memory-leak-in-copy.patch \
            file://memory-leak2.patch \
            file://double-free.patch \
            file://pull-in-string.h.patch \
            file://thinko-in-restore.patch"

SRC_URI[md5sum] = "d132c119831c27350e10b9f885711adc"
SRC_URI[sha256sum] = "9f6214b8e53f4bba651ac5a72c0f6193b12aa21fbf1d675d89a7b4bc45264498"
