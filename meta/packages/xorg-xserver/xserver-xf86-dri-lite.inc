require xserver-xf86-common.inc

PROTO_DEPS = "randrproto renderproto fixesproto damageproto xextproto xproto xf86dgaproto xf86miscproto xf86rushproto xf86vidmodeproto xf86bigfontproto compositeproto recordproto resourceproto videoproto scrnsaverproto evieext trapproto xineramaproto fontsproto kbproto inputproto bigreqsproto xcmiscproto glproto xf86driproto dri2proto"

LIB_DEPS = "pixman mesa-dri libxfont xtrans libxau libxext libxdmcp libdrm libxkbfile libpciaccess openssl"

DEPENDS = "${PROTO_DEPS} ${LIB_DEPS}"

RDEPENDS = "hal xorg-minimal-fonts"

FILES_${PN} += "${bindir}/Xorg "

EXTRA_OECONF += "--disable-cfb \
                 --disable-afb \
                 --disable-mfb \
                 --disable-dga \
                 --disable-xinerama \
                 --disable-xf86misc \
                 --disable-xorgcfg \
                 --disable-dmx \
                 --disable-xnest \
                 --disable-xvfb \
                 --enable-composite \
                 --enable-dri \
                 --enable-glx-tls \
                 --sysconfdir=/etc/X11 \
                 --localstatedir=/var \
                 --with-fontdir=/usr/share/fonts/X11 \
                 --with-xkb-output=/var/lib/xkb \
                 ac_cv_file__usr_share_sgml_X11_defs_ent=no"

# Due to mesa-dri
COMPATIBLE_HOST = '(i.86.*-linux)'
