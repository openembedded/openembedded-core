include mesa-common.inc

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PV = "7.3.0+git${SRCREV}"
PR = "r3"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

# Netbooks have DRI support so use mesa-dri by default
DEFAULT_PREFERENCE_netbook = "1"

SRC_URI = "git://anongit.freedesktop.org/git/mesa/mesa;protocol=git;branch=intel-2008-q3 "
S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-xprogs"
PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
FILES_${PN}-xprogs = "${bindir}/glxdemo ${bindir}/glxgears ${bindir}/glxheads ${bindir}/glxinfo"

EXTRA_OECONF += "--with-driver=dri"

do_install_append () {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/progs/xdemos/{glxdemo,glxgears,glxheads,glxinfo} ${D}/usr/bin/
}

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(bb.data.getVar('libdir', d, 1), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

COMPATIBLE_HOST = '(i.86.*-linux)'
