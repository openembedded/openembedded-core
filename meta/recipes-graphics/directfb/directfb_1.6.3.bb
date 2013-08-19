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

SRC_URI[md5sum] = "641e8e999c017770da647f9b5b890906"
SRC_URI[sha256sum] = "7a96aced0f69b2ec0810e9923068e61c21e6b19dd593e09394c872414df75e70"
