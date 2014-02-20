require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"

SRC_URI[md5sum] = "e16c14b27c644b8e0dd72bdb5ff77450"
SRC_URI[sha256sum] = "de3097fdc36d624ea6cf4bb853402fde781acdb860f12152c5eb879777389882"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
