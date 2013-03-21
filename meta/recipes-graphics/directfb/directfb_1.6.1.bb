require directfb.inc

RV = "1.6-0"
PR = "${INC_PR}.2"

DEPENDS += "sysfsutils"

SRC_URI += "file://fix-compilation-with-zlib.patch \
            file://rename-no-instrument-function-macro.patch \
            file://fixsepbuild.patch"

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

LEAD_SONAME = "libdirectfb-1.6.so.0"

SRC_URI[md5sum] = "76d3066e75664aa79204af545f2f3c65"
SRC_URI[sha256sum] = "f47575ea35dd8a30e548c04bf52d8565756d0bed45d1cf9f8afac1cf9b521c45"

