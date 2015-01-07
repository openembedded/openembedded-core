require git.inc

SRC_URI[md5sum] = "ff41fdb094eed1ec430aed8ee9b9849c"
SRC_URI[sha256sum] = "367a77d0b10a5070b02a0fb0e942f26f25af61793128e0ddfd5c5c474de93589"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
