require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "c07ea15d5d0c31544de265f567d6e5ef"
SRC_URI[sha256sum] = "44392f5bfcb17aa943338469ddafd94a9091f8b33eb5d3a2abb60f2bb61d1a73"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
