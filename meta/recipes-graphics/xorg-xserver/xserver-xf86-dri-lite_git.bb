require xserver-xf86-dri-lite.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=74df27b6254cc88d2799b5f4f5949c00"

SRCREV = "6689e8add183835274a70ee013e5d3bc8023681f"
PE = "1"
PR = "r0"
PV = "1.10+git${SRCPV}"

PROTO_DEPS += "xf86driproto dri2proto"

DEPENDS += "font-util"

RDEPENDS_${PN} += "xkeyboard-config"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/xserver;protocol=git;branch=master \
           file://crosscompile.patch; \
           file://fix_macros1.patch;"

# Misc build failure for master HEAD
SRC_URI += "file://fix_open_max_preprocessor_error.patch;"

EXTRA_OECONF += "--enable-dri --enable-dri2 --enable-dga"

S = "${WORKDIR}/git"
