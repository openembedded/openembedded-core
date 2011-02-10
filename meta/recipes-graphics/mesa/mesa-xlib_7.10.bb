include mesa-common.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://talloc-removal.patch \
           "

SRC_URI[md5sum] = "33fb94eccc02cbb4d8d1365615e38e46"
SRC_URI[sha256sum] = "bcf28f43f39c28da271c0f5857fb32898d4ade3e035e80a0ceece1c2df6e0aca"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native"

PE = "1"
PR = "r1"

EXTRA_OECONF += "--with-driver=xlib"
