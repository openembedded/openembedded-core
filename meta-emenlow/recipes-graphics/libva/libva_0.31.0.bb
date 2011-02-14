DESCRIPTION = "Video Acceleration (VA) API for Linux"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=efc13a0998e678466e556756613c582e"
PR = "r1"
DEPENDS = "libxfixes libxext"

inherit autotools

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/libva_0.31.0-1+sds9.1ubuntu1.tar.gz \
	file://033_g45_add_rgba_subpic.patch \
	file://034_g45_fix_return_for_unimpl.patch \
	file://035_g45_add_yv12_image_format.patch \
	file://036_g45_add_vaGetImage.patch \
	file://037_g45_add_vaPutImage.patch \
	file://038_g45_vaPutSurface_cliprects.patch \
	file://102_attribute_visibility.patch \
	file://103_fix_vainfo_deps.patch \
	file://104_fix_libva_pkgconfig_deps.patch \
	file://105_dont_search_LIBGL_DRIVERS_PATH.patch \
	file://108_drivers_path.patch \
	file://203_fix_fglrx_detection.patch \
	file://204_check_ATIFGLEXTENSION.patch \
	file://300_sds_version.patch \
	file://301_vdpau_mpeg4.patch \
	file://320_move_vaPutSurface_flags_def.patch \
	file://321_libva_glx.base.patch \
	file://322_libva_glx.patch \
	file://390_compat.base.patch \
	file://391_compat.patch \
	file://392_compat.dso.patch \
	file://libdrm-poulsbo.patch"

export LDFLAGS="-Wl,-z,defs"

EXTRA_OECONF = "--disable-i965-driver"

FILES_${PN} += "${libdir}/va/drivers/*"

COMPATIBLE_MACHINE = "emenlow"
PACKAGE_ARCH = "${MACHINE_ARCH}"
