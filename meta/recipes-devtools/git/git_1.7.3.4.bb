require git.inc

PR = "r0"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "

SRC_URI[md5sum] = "3a2602016f98c529cda7b9fad1a6e216"
SRC_URI[sha256sum] = "3e5e2b6547ee4aae82b4c5f589ff084996e9e6e0e2b52c92365e6baa1e4a0171"
