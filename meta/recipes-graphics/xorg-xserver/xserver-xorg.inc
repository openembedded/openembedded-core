require xserver-xorg-common.inc

PROTO_DEPS += "xf86driproto dri2proto"
LIB_DEPS += "virtual/libgl"

EXTRA_OECONF += "\
                 --enable-dri \
                 --enable-dri2 \
                 --enable-glx-tls \
                 --with-pic \
                 --with-int10=x86emu \
"
