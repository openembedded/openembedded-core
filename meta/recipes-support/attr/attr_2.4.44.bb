require attr.inc

PR = "r1"

SRC_URI += "file://memory-leak-in-copy.patch \
            file://memory-leak2.patch \
            file://double-free.patch \
            file://pull-in-string.h.patch \
            file://thinko-in-restore.patch"
