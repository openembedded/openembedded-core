require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- Intel i8xx, i9xx display driver"
DEPENDS += "virtual/libx11 libxvmc drm dri2proto glproto \
	    virtual/libgl xineramaproto libpciaccess"
PROVIDES = "xf86-video-intel"

PR = "r1"
PV = "2.4.97.0+git${SRCREV}"

#TODO: Investigate the fact that these patches fail to apply to git master a.t.m
#file://002_avoid_duplicate_SaveHWState.patch;patch=1 
#file://004_reduce_driver_boottime.patch;patch=1
SRC_URI = "git://anongit.freedesktop.org/git/xorg/driver/xf86-video-intel;protocol=git \
           file://005_disable_sdvo_TV_port_restoreHW.patch;patch=1 \
           file://006_disable_check_lvds_panelpower_status.patch;patch=1"

S = "${WORKDIR}/git"

COMPATIBLE_HOST = '(i.86.*-linux)'

EXTRA_OECONF = "--enable-dri --disable-static"

