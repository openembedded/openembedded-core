require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "d91b6099fb6763cf92c696977a247060"
SRC_URI[sha256sum] = "f1d3c6d683f8f15035a8d0f3eb30ed160ff6607111dfc6026d8836307aebc53a"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
