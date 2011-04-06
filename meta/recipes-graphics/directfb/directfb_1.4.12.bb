require directfb.inc

RV = "1.4-5"
PR = "r0"

DEPENDS += "sysfsutils"

EXTRA_OECONF = "\
  --enable-freetype=yes \
  --enable-zlib \
  --with-gfxdrivers=none \
  --disable-sdl \
  --disable-vnc \
  --disable-x11 \
"

LEAD_SONAME = "libdirectfb-1.4.so.5"

SRC_URI[md5sum] = "2c779c9a8456790c6c29ad85459b2600"
SRC_URI[sha256sum] = "b119ab9c5c0c505c23e32d41ae54bd04cb474c5e58900ec0f1cf9482f892f9b2"


