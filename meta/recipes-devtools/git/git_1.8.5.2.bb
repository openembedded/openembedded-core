require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"

SRC_URI[md5sum] = "df8519044f9880f3687d863d99245282"
SRC_URI[sha256sum] = "024694524b8be91fe29afca65bc3a9e40de1322dd5ef2b2c0babb6849c913e8a"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
