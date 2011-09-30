include mesa-common.inc

LIC_FILES_CHKSUM = "file://docs/license.html;md5=7a3373c039b6b925c427755a4f779c1d"

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat \
            libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native python-native"

PR = "r2"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://crossfix.patch \
           file://uclibc.patch \
          "

SRC_URI[md5sum] = "f5de82852f1243f42cc004039e10b771"
SRC_URI[sha256sum] = "8ced2678ce11cf30804694a92ea3ca6b82f158ae8995bdc626c7e85aac71c7c1"

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
