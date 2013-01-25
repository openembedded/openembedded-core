require git.inc

PR = "r1"

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "1aca109d4a719fe5bc43d25927fbc7d9"
SRC_URI[sha256sum] = "c06d5d29389e90583d16ec41d5be931593196f6842f9b14e910a0b5781c10e07"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
