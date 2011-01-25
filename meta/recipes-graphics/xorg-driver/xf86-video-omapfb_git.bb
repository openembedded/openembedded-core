require xf86-driver-common.inc

DESCRIPTION = "X.Org X server -- OMAP display driver"
LICENSE = "MIT-X"
LIC_FILES_CHKSUM = "file://src/omapfb-driver.c;beginline=1;endline=30;md5=a44c2a37e04d1c2c5f0313afb493f833"
DEPENDS += "virtual/libx11"

PE = "1"
PR = "r1"
PV = "0.1.1+git${SRCPV}"

SRC_URI = "git://git.pingu.fi/xf86-video-omapfb.git;protocol=http \
            file://omap-revert-set-CRTC-limit.patch \
            file://omap-revert-set-virtual-size.patch \
            file://omap-force-plain-mode.patch  \
            file://omap-blacklist-tv-out.patch  \
	"

S = "${WORKDIR}/git"

EXTRA_OECONF_armv7a = " --enable-neon "
CFLAGS += " -I${STAGING_INCDIR}/xorg "

# Use overlay 2 on omap3 to enable other apps to use overlay 1 (e.g. dmai or omapfbplay)
do_compile_prepend_armv7a () {
	sed -i -e s:fb1:fb2:g ${S}/src/omapfb-xv.c
}
