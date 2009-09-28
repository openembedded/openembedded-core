require xf86-driver-common.inc

DESCRIPTION = "X.Org X server -- OMAP display driver"
DEPENDS += "virtual/libx11"

PE = "1"
PR = "r0"
PV = "0.1.1+git${SRCPV}"

SRC_URI = "git://git.pingu.fi/xf86-video-omapfb.git;protocol=http \
	"

S = "${WORKDIR}/git"

EXTRA_OECONF_armv7a = " --enable-neon "
CFLAGS += " -I${STAGING_INCDIR}/xorg "

# Use overlay 2 on omap3 to enable other apps to use overlay 1 (e.g. dmai or omapfbplay)
do_compile_prepend_armv7a () {
	sed -i -e s:fb1:fb2:g ${S}/src/omapfb-xv.c
}

