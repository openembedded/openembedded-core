require xserver-xf86-dri-lite.inc

PE = "1"
PR = "r0"
PV = "1.7.99+git${SRCPV}"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

RDEPENDS_${PN} += "xkeyboard-config"

#           file://cache-xkbcomp-output-for-fast-start-up.patch;patch=1
#           file://xserver-1.5.0-bg-none-root.patch;patch=1
#           file://xserver-no-root-2.patch;patch=1
#           file://xserver-boottime.patch;patch=1
#           file://xserver-DRI2Swapbuffer.patch;patch=1


SRC_URI = "git://anongit.freedesktop.org/git/xorg/xserver;protocol=git;branch=master \
           file://nodolt.patch;patch=1 \
           file://crosscompile.patch;patch=1 \
           file://fix_macros1.patch;patch=1"
#           file://libdri-xinerama-symbol.patch;patch=1

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;patch=1"

EXTRA_OECONF += "--enable-dri --enable-dri2 --enable-dga"

S = "${WORKDIR}/git"
