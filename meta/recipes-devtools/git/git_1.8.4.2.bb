require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "6f63d103465b86ca0ebe4189ea54731c"
SRC_URI[sha256sum] = "6732278ce4870b314cfd83500ec15d5ee7a868be5b96549022d6f0972bf3dd2a"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
