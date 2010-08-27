require gmp.inc

PR = "r0"

SRC_URI_append += "file://sh4-asmfix.patch \
                   file://use-includedir.patch "

