require xserver-xf86-common.inc

PROVIDES += "xserver-xf86-lite"

PROTO_DEPS = "randrproto renderproto fixesproto damageproto xextproto xproto xf86dgaproto xf86miscproto xf86rushproto xf86vidmodeproto xf86bigfontproto compositeproto recordproto resourceproto videoproto scrnsaverproto evieext trapproto xineramaproto fontsproto kbproto inputproto bigreqsproto xcmiscproto glproto"

LIB_DEPS = "mesa pixman libxfont xtrans libxau libxext libxdmcp libxt libdrm-psb libxkbfile libpciaccess openssl"

DEPENDS = "${PROTO_DEPS} ${LIB_DEPS}"

RDEPENDS = "hal xorg-minimal-fonts"

FILES_${PN} += "${bindir}/Xorg "

EXTRA_OECONF += "--disable-acfb \
	         --enable-aiglx=yes \
                 --enable-dri=yes \
                 --enable-glx=yes \
                 --enable-glx-tls=yes \
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
                 --sysconfdir=/etc/X11 \
                 --localstatedir=/var \
                 --with-pic \
                 --with-int10=x86emu \
                 --with-fontdir=/usr/share/fonts/X11 \
                 --with-xkb-output=/var/lib/xkb \
                 ac_cv_file__usr_share_sgml_X11_defs_ent=no"
