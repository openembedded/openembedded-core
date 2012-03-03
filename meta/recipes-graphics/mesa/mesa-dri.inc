PROTO_DEPS += "dri2proto"
LIB_DEPS += "libdrm expat"

# most of our targets do not have DRI so will use mesa-xlib
DEFAULT_PREFERENCE = "-1"

DRIDRIVERS = "swrast"
DRIDRIVERS_append_x86 = ",i915,i965"
DRIDRIVERS_append_x86-64 = ",i915,i965"

EXTRA_OECONF += "--with-driver=dri --disable-egl --disable-gallium --without-gallium-drivers --with-dri-drivers=${DRIDRIVERS}"

python populate_packages_prepend() {
	import os.path

	dri_drivers_root = os.path.join(d.getVar('libdir', True), "dri")

	do_split_packages(d, dri_drivers_root, '^(.*)_dri\.so$', 'mesa-dri-driver-%s', 'Mesa %s DRI driver', extra_depends='')
}

PACKAGES_DYNAMIC = "mesa-dri-driver-*"

FILES_${PN}-dbg += "${libdir}/dri/.debug/*"

