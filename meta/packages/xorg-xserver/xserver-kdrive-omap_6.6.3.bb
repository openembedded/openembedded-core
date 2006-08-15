LICENSE = "MIT"
DEPENDS = "xsp tslib-maemo xproto libxdmcp xextensions xtrans libxau virtual/libx11 libxext libxrandr fixesext damageext libxfont resourceext compositeext xcalibrateext recordext"
PROVIDES = "virtual/xserver"
PACKAGES = "${PN} ${PN}-doc ${PN}-dev ${PN}-locale"
SECTION = "x11/base"
DESCRIPTION = "X server from freedesktop.org"
DESCRIPTION_xserver-kdrive-omap = "X server from freedesktop.org with patches from maemo.org, supporting X on OMAP based devices"

PR = "r2"

FILES_${PN} = "${bindir}/Xomap"

SRC_URI = " http://stage.maemo.org/pool/maemo/ossw/source/x/xserver-kdrive/xserver-kdrive_${PV}-5.tar.gz \
	file://kmode.patch;patch=1 \
	file://fbdev-not-fix.patch;patch=1 \
        file://configure-tslib.patch;patch=1;pnum=0"

S = "${WORKDIR}/xserver"

inherit autotools pkgconfig 

EXTRA_OECONF = "--enable-composite --disable-xinerama --enable-xomapserver \
                --enable-xsp --disable-xlocale --disable-rpath --prefix=/usr"
