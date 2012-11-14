require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Texas Instruments OMAP framebuffer driver"

DESCRIPTION = "omap driver supports the basic Texas Instruments OMAP \
framebuffer."

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10ce5de3b111315ea652a5f74ec0c602"
DEPENDS += "virtual/libx11 libdrm"

RPROVIDES = "xf86-video-omapfb"
RCONFLICTS = "xf86-video-omapfb"
RREPLACES = "xf86-video-omapfb"

SRCREV = "ae0394e687f1a77e966cf72f895da91840dffb8f"
PR = "${INC_PR}.0"
PV = "0.4.2+gitr${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/xorg/driver/xf86-video-omap;protocol=git \
"

S = "${WORKDIR}/git"

EXTRA_OECONF_armv7a = " --enable-neon "
CFLAGS += " -I${STAGING_INCDIR}/xorg "

# Use overlay 2 on omap3 to enable other apps to use overlay 1 (e.g. dmai or omapfbplay)
do_compile_prepend_armv7a () {
        sed -i -e s:fb1:fb2:g ${S}/src/omap_xv.c
}
