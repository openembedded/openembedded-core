DESCRIPTION = "X server for glamo chip in GTA02"
SECTION = "x11/base"
LICENSE = "MIT"
DEPENDS = "compositeproto damageproto fixesproto recordproto resourceproto \
           scrnsaverproto xineramaproto videoproto xextproto xproto \
           libxau libxext libxdmcp libxfont libxrandr tslib virtual/libx11 \
           xtrans libxkbfile libxcalibrate"
DEPENDS += "libxkbfile libxcalibrate"
RDEPENDS_${PN} = "xserver-kdrive"
PROVIDES = "virtual/xserver"
PE = "1"
PR = "r6"
PV = "1.3.0.0+git${SRCREV}"

SRC_URI = "git://people.freedesktop.org/~dodji/xglamo;protocol=git \
        file://kmode.patch;patch=1 \
        file://disable-apm.patch;patch=1 \
        file://no-serial-probing.patch;patch=1 \
        file://fbdev-not-fix.patch;patch=1  \
        file://optional-xkb.patch;patch=1 \
        file://enable-tslib.patch;patch=1 \
        file://kmode-palm.patch;patch=1 \
        file://enable-epson.patch;patch=1 \
        file://enable-builtin-fonts.patch;patch=1 \
        file://kdrive-evdev.patch;patch=1  \
        file://kdrive-use-evdev.patch;patch=1  \
        file://disable-xf86-dga-xorgcfg.patch;patch=1 \
        file://fix_default_mode.patch;patch=1 \
        file://enable-xcalibrate.patch;patch=1 \
        file://hide-cursor-and-ppm-root.patch;patch=1 \
        file://xcalibrate_coords.patch;patch=1 \
        file://w100.patch;patch=1 \
        file://w100-autofoo.patch;patch=1 \
        file://w100-fix-offscreen-bmp.patch;patch=1 \
        file://kdrive-1.3-18bpp.patch;patch=1 \
        file://gumstix-kmode.patch;patch=1 \
        file://fix-picturestr-include-order.patch;patch=1 \
"

FILESPATH = "${FILE_DIRNAME}/xserver-kdrive-glamo:${FILE_DIRNAME}/xserver-kdrive-1.3.0.0:${FILE_DIRNAME}/xserver-kdrive:${FILE_DIRNAME}/files"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-composite --enable-kdrive \
                --disable-dga --disable-dri --disable-xinerama \
                --disable-xf86misc --disable-xf86vidmode \
                --disable-xorg --disable-xorgcfg \
                --disable-xkb --disable-xnest --disable-xvfb \
                --disable-xevie --disable-xprint --disable-xtrap \
                --disable-dmx \
                --with-default-font-path=built-ins \
                --enable-tslib --enable-xcalibrate \
                ac_cv_file__usr_share_sgml_X11_defs_ent=no"

do_configure_prepend() {
    sed -i -e 's/tslib-0.0/tslib-1.0/' ${S}/configure.ac
}

FILES_${PN} = "${bindir}/Xglamo"
FILES_${PN}-dbg = "${bindir}/.debug/Xglamo"

ARM_INSTRUCTION_SET = "arm"
