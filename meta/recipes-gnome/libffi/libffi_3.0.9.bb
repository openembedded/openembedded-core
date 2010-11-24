SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa09cb778aaba64dc9eac37ab7e4e5d8"
inherit autotools

BBCLASSEXTEND = "native"
