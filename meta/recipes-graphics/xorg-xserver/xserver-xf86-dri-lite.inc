require xserver-xf86-common.inc

PROTO_DEPS = "randrproto renderproto fixesproto damageproto xextproto xproto xf86dgaproto xf86miscproto xf86rushproto xf86vidmodeproto xf86bigfontproto compositeproto recordproto resourceproto videoproto scrnsaverproto evieext trapproto xineramaproto fontsproto kbproto inputproto bigreqsproto xcmiscproto glproto"

LIB_DEPS = "pixman virtual/libgl libxfont xtrans libxau libxext libxdmcp libdrm libxkbfile libpciaccess openssl"

DEPENDS = "${PROTO_DEPS} ${LIB_DEPS}"

RDEPENDS_${PN} += "hal xorg-minimal-fonts"

FILES_${PN} += "${bindir}/Xorg "

EXTRA_OECONF += "--disable-static \
                 --disable-acfb \
                 --disable-ccfb \
                 --disable-mcfb \
                 --disable-dga \
                 --disable-xinerama \
                 --disable-xf86misc \
                 --disable-xorgcfg \
                 --disable-record \
                 --disable-dmx \
                 --disable-xnest \
                 --disable-xvfb \
                 --enable-composite \
                 --enable-glx-tls \
                 --sysconfdir=/etc/X11 \
                 --localstatedir=/var \
                 --with-pic \
                 --with-int10=x86emu \
                 --with-fontdir=${datadir}/fonts/X11 \
                 --with-xkb-output=/var/lib/xkb \
                 ac_cv_file__usr_share_sgml_X11_defs_ent=no"

# Due to mesa-dri
COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

