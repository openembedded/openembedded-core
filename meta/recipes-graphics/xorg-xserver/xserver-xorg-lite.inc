require xserver-xorg-common.inc

EXTRA_OECONF += "\
                 --disable-dri \
                 --disable-glx \
                 --disable-dga \
"
