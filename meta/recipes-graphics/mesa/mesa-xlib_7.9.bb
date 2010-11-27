include mesa-common.inc

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes talloc libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PE = "1"
PR = "r1"

EXTRA_OECONF += "--with-driver=xlib"
