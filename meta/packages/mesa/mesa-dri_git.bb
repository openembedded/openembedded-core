include mesa-common.inc

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PV = "7.3.0+git${SRCREV}"
PR = "r0"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

# ASUS EeePC 901 has DRI support so use mesa-dri by default
DEFAULT_PREFERENCE_eee901 = "1"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git"
S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-xprogs"

FILES_${PN} += "${libdir}/dri/*.so"
FILES_${PN}-dbg += "${libdir}/dri/.debug/*"

EXTRA_OECONF += "--with-driver=dri --with-dri-drivers=${MACHINE_DRI_MODULES}"

COMPATIBLE_HOST = '(i.86.*-linux)'
