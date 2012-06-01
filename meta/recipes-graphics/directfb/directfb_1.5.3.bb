require directfb.inc

RV = "1.5-0"
PR = "r0"

DEPENDS += "sysfsutils"

SRC_URI += "file://libdirect-Use-ARM-assembler-for-atomic-operations-on.patch \
            file://libdirect-remove-include-of-linux-config.h.patch"

EXTRA_OECONF = "\
  --enable-freetype=yes \
  --enable-zlib \
  --with-gfxdrivers=none \
  --disable-sdl \
  --disable-vnc \
  --disable-x11 \
"

LEAD_SONAME = "libdirectfb-1.5.so.0"

SRC_URI[md5sum] = "54a9ec931c8e3c82adb924194e65120e"
SRC_URI[sha256sum] = "e57575e8bb5f6452db6d5d54d78e3a460bc08bf50b1fa10d0250936dbe2251f0"
