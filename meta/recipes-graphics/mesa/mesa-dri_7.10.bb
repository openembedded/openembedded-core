include mesa-common.inc

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat \
            libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS}"

PR = "r1"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://talloc-removal.patch \
           file://crossfix.patch"

SRC_URI[md5sum] = "33fb94eccc02cbb4d8d1365615e38e46"
SRC_URI[sha256sum] = "bcf28f43f39c28da271c0f5857fb32898d4ade3e035e80a0ceece1c2df6e0aca"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

LEAD_SONAME = "libGL.so.1"

EXTRA_OECONF += "--with-driver=dri --disable-egl --disable-gallium"

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(bb.data.getVar('libdir', d, 1), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
