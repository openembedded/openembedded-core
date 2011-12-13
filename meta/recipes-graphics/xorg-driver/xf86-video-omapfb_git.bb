require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Texas Instruments OMAP framebuffer driver"

DESCRIPTION = "omapfb driver supports the basic Texas Instruments OMAP \
framebuffer."

LICENSE = "MIT-X & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=63e2cbac53863f60e2f43343fb34367f"
DEPENDS += "virtual/libx11"

SRCREV = "28c006c94e57ea71df11ec4fff79d7ffcfc4860f"
PR = "${INC_PR}.6"
PV = "0.1.1+gitr${SRCPV}"

SRC_URI = "git://git.pingu.fi/xf86-video-omapfb;protocol=http \
            file://omap-revert-set-CRTC-limit.patch \
            file://omap-revert-set-virtual-size.patch \
            file://omap-force-plain-mode.patch  \
            file://omap-blacklist-tv-out.patch  \
            file://0004-Attempt-to-fix-VRFB.patch \
"

S = "${WORKDIR}/git"

EXTRA_OECONF_armv7a = " --enable-neon "
CFLAGS += " -I${STAGING_INCDIR}/xorg "

# Use overlay 2 on omap3 to enable other apps to use overlay 1 (e.g. dmai or omapfbplay)
do_compile_prepend_armv7a () {
        sed -i -e s:fb1:fb2:g ${S}/src/omapfb-xv.c
}
