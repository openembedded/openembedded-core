include mesa-common.inc

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PE = "1"
PR = "r3"

EXTRA_OECONF += "--with-driver=xlib"

PACKAGES =+ "${PN}-xprogs"

FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

do_install_append () {
    install -d ${D}/${bindir}
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/${bindir}
}
