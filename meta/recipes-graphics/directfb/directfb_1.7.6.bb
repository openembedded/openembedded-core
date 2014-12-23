require directfb.inc

RV = "1.7-6"

DEPENDS += "sysfsutils"

EXTRA_OECONF = "\
  --enable-freetype=yes \
  --enable-zlib \
  --with-gfxdrivers=none \
  --disable-sdl \
  --disable-vnc \
  --disable-x11 \
  --disable-imlib2 \
  --disable-mesa \
"

LEAD_SONAME = "libdirectfb-1.7.so.0"

SRC_URI[md5sum] = "8a7bb06b3f58599b230b4cf314004512"
SRC_URI[sha256sum] = "44f32bacfb842ea234599532f8481fe41b5bd2310d2bd101508eb3a5df26c9e1"
