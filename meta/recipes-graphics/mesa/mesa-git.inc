SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://cross2.patch \
           file://matypes.h"
#           file://mesa-DRI2Swapbuffer.patch "
S = "${WORKDIR}/git"

SRCREV = "1bf94d419805538ac23a4d0b04d31ac5e4487aca"
PV = "7.7+git${SRCPV}"

PACKAGES =+ "${PN}-xprogs"

FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

do_configure_prepend () {
    cp ${WORKDIR}/matypes.h ${S}/src/mesa/x86
    touch ${S}/src/mesa/x86/matypes.h
}

do_compile () {
	oe_runmake clean
	oe_runmake -C src/glsl CC='${BUILD_CC}' CFLAGS=""
	mv ${S}/src/glsl/apps/compile ${S}/host_compile
	oe_runmake clean
	oe_runmake GLSL_CL="${S}/host_compile"
}

do_install_append () {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/usr/bin/
}
