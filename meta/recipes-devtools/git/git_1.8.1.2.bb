require git.inc

PR = "r0"

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "9f912370a1831c851ff8df171e8a7218"
SRC_URI[sha256sum] = "07d210a37deec465b4d00e4907c732dee5c5d23c7d90c1b582e7ab754bcf24c7"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
