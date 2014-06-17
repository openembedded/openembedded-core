require directfb.inc

RV = "1.7-4"

DEPENDS += "sysfsutils"

SRC_URI += "file://fixsepbuild.patch"

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

SRC_URI[md5sum] = "f5bdacde77fc653279819796ae11341e"
SRC_URI[sha256sum] = "20ccd60011c788e50c940ab566943d050679067bc84dc37ca447f1b4af08481b"
