include mesa-common.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git \
           file://cross2.patch \
           file://matypes.h"
#           file://mesa-DRI2Swapbuffer.patch "
S = "${WORKDIR}/git"

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

SRCREV = "1bf94d419805538ac23a4d0b04d31ac5e4487aca"
PV = "7.7+git${SRCPV}"
PR = "r1"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

PACKAGES =+ "${PN}-xprogs"
PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

LEAD_SONAME = "libGL.so.1"

EXTRA_OECONF += "--with-driver=dri --disable-egl --disable-gallium"

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

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(bb.data.getVar('libdir', d, 1), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'
