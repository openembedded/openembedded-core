require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- PIntel i8xx, i9xx display driver"
DEPENDS += "virtual/libx11 libxvmc libdrm-psb dri2proto glproto \
	    virtual/libgl xineramaproto libpciaccess"
RDEPENDS += linux-psb-modules

PV = "0.2.6+git${SRCREV}"
PR = "r4"

SRC_URI = "git://git.moblin.org/projects/xf86-video-psb;protocol=git \
           file://104_disable_locks.diff;patch=1"

S = "${WORKDIR}/git"

COMPATIBLE_HOST = '(i.86.*-linux)'
COMPATIBLE_MACHINE = "menlow"

EXTRA_OECONF = "--enable-dri --disable-static"

