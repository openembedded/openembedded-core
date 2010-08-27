include mesa-common.inc

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto"
LIB_DEPS = "virtual/libx11 libxext libxxf86vm libxdamage libxfixes"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PE = "1"
PR = "r0"

EXTRA_OECONF += "--with-driver=xlib"

do_install_append () {
    install -d ${D}/${bindir}
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/${bindir}
}

PACKAGES =+ "${PN}-xprogs"

FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"
