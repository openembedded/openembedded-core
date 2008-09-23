include mesa-common.inc

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PE = "1"
PR = "r3"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

# ASUS EeePC 901 has DRI support so use mesa-dri by default
DEFAULT_PREFERENCE_eee901 = "1"

PACKAGES =+ "${PN}-xprogs"

FILES_${PN} += "${libdir}/dri/*.so"
FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

EXTRA_OECONF += "--with-driver=dri --with-dri-drivers=${MACHINE_DRI_MODULES}"

do_install_append () {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/usr/bin/
}
