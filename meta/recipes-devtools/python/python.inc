DESCRIPTION = "The Python Programming Language"
HOMEPAGE = "http://www.python.org"
LICENSE = "PSFv2"
SECTION = "devel/python"
# bump this on every change in contrib/python/generate-manifest-2.7.py
INC_PR = "r0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=2dffb81509b47974467ea23409909b1c"

SRC_URI = "http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.bz2"
 
SRC_URI[md5sum] = "ba7b2f11ffdbf195ee0d111b9455a5bd"
SRC_URI[sha256sum] = "5057eb067eb5b5a6040dbd0e889e06550bde9ec041dadaa855ee9490034cbdab"

PYTHON_MAJMIN = "2.7"

inherit autotools

EXTRA_OECONF = "\
  --with-threads \
  --with-pymalloc \
  --with-cyclic-gc \
  --without-cxx \
  --with-signal-module \
  --with-wctype-functions \
  --enable-shared \
"

EXTRA_OECONF_linuxstdbase = "\
  --with-threads \
  --with-pymalloc \
  --with-cyclic-gc \
  --without-cxx \
  --with-signal-module \
  --enable-shared \
  ac_cv_sizeof_off_t=8 \
"
