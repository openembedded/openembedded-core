include mesa-common.inc
include mesa-${PV}.inc

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native"

PR = "${INC_PR}.0"

EXTRA_OECONF += "--with-driver=xlib --without-gallium-drivers"
