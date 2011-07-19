DESCRIPTION = "The Python Programming Language"
HOMEPAGE = "http://www.python.org"
LICENSE = "PSF"
SECTION = "devel/python"
# bump this on every change in contrib/python/generate-manifest-2.6.py
INC_PR = "r2"

DEFAULT_PREFERENCE = "-26"

PYTHON_MAJMIN = "2.6"

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

PARALLEL_MAKE = ""
