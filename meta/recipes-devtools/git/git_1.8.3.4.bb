require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "80eec3201a5d012913d287b85adaee8e"
SRC_URI[sha256sum] = "dfa2cdf2df92b4abe956b1e7586030381c4e39e89161ab789a660d8d1f24d9d9"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
