require xserver-xorg-common.inc

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
                 --disable-dri \
                 --disable-glx \
                 --sysconfdir=/etc/X11 \
                 --localstatedir=/var \
                 --with-fontdir=${datadir}/fonts/X11 \
                 --with-xkb-output=/var/lib/xkb \
                 ac_cv_file__usr_share_sgml_X11_defs_ent=no"
