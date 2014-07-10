require git.inc

SRC_URI[md5sum] = "981f5937840716cb563be1cc6292c8d7"
SRC_URI[sha256sum] = "02609a06fb40db1f6a968867c0e82bcb959b85902747830de0fda53228712daf"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
