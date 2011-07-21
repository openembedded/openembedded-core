include mesa-common.inc

FILESPATH =. "${FILE_DIRNAME}/mesa-dri:"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://uclibc.patch \
           "

SRC_URI[md5sum] = "f5de82852f1243f42cc004039e10b771"
SRC_URI[sha256sum] = "8ced2678ce11cf30804694a92ea3ca6b82f158ae8995bdc626c7e85aac71c7c1"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native"

PE = "1"
PR = "r1"

EXTRA_OECONF += "--with-driver=xlib"
