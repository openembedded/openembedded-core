include mesa-common.inc
include mesa-${PV}.inc

PROTO_DEPS = "xf86driproto glproto dri2proto"
LIB_DEPS = "libdrm virtual/libx11 libxext libxxf86vm libxdamage libxfixes expat \
            libxml2-native"

DEPENDS = "${PROTO_DEPS}  ${LIB_DEPS} makedepend-native python-native"

PR = "${INC_PR}.0"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

LEAD_SONAME = "libGL.so.1"

EXTRA_OECONF += "--with-driver=dri --disable-egl -with-dri-drivers=swrast,i915,i965 --without-gallium-drivers"

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(bb.data.getVar('libdir', d, 1), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"
