include mesa-common.inc

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PV = "7.2+git${SRCREV}"
PR = "r1"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git"
S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/dri/*.so"
FILES_${PN}-dbg += "${libdir}/dri/.debug/*"

EXTRA_OECONF += "--with-driver=dri --with-dri-drivers=${MACHINE_DRI_MODULES}"

