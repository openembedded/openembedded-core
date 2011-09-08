require xserver-xorg-common.inc

PROTO_DEPS += "xf86driproto dri2proto"
LIB_DEPS += "virtual/libgl"

require ../mesa/glx-use-tls.inc

EXTRA_OECONF += "\
                 --enable-dri \
                 --enable-dri2 \
                 --with-pic \
                 --with-int10=x86emu \
"
