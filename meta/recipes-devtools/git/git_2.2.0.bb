require git.inc

SRC_URI[md5sum] = "2d5bbcc3e887cc4ba499f80420e2d5f7"
SRC_URI[sha256sum] = "bea9548f5a39daaf7c3873b6a5be47d7f92cbf42d32957e1be955a2e0e7b83b4"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
