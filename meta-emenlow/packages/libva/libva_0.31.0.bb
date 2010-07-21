DESCRIPTION = "Video Acceleration (VA) API for Linux"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=efc13a0998e678466e556756613c582e"
PR = "r0"

inherit autotools

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/libva_0.31.0-1+sds9.1ubuntu1.tar.gz \
	file://033_g45_add_rgba_subpic.patch;patch=1 \
	file://034_g45_fix_return_for_unimpl.patch;patch=1 \
	file://035_g45_add_yv12_image_format.patch;patch=1 \
	file://036_g45_add_vaGetImage.patch;patch=1 \
	file://037_g45_add_vaPutImage.patch;patch=1 \
	file://038_g45_vaPutSurface_cliprects.patch;patch=1 \
	file://102_attribute_visibility.patch;patch=1 \
	file://103_fix_vainfo_deps.patch;patch=1 \
	file://104_fix_libva_pkgconfig_deps.patch;patch=1 \
	file://105_dont_search_LIBGL_DRIVERS_PATH.patch;patch=1 \
	file://108_drivers_path.patch;patch=1 \
	file://203_fix_fglrx_detection.patch;patch=1 \
	file://204_check_ATIFGLEXTENSION.patch;patch=1 \
	file://300_sds_version.patch;patch=1 \
	file://301_vdpau_mpeg4.patch;patch=1 \
	file://320_move_vaPutSurface_flags_def.patch;patch=1 \
	file://321_libva_glx.base.patch;patch=1 \
	file://322_libva_glx.patch;patch=1 \
	file://390_compat.base.patch;patch=1 \
	file://391_compat.patch;patch=1 \
	file://392_compat.dso.patch;patch=1"

export LDFLAGS="-Wl,-z,defs"

EXTRA_OECONF = "--disable-i965-driver"

FILES_${PN} += "${libdir}/va/drivers/*"
