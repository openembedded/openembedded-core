require directfb.inc

RV = "1.7-1"

DEPENDS += "sysfsutils"

SRC_URI += "file://fix-compilation-with-zlib.patch \
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

LEAD_SONAME = "libdirectfb-1.7.so.0"

SRC_URI[md5sum] = "0ef44c16a414312fd38d6764e2cb9893"
SRC_URI[sha256sum] = "dd7de38b3cd1408411b37ca28ec73e14b7672e5e28256b7bf91826240c81f519"
