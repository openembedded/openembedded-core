require git.inc

SRC_URI[tarball.md5sum] = "ff41fdb094eed1ec430aed8ee9b9849c"
SRC_URI[tarball.sha256sum] = "367a77d0b10a5070b02a0fb0e942f26f25af61793128e0ddfd5c5c474de93589"
SRC_URI[manpages.md5sum] = "b5ddd262b608804ba4403f0f82d881d7"
SRC_URI[manpages.sha256sum] = "69dcb3decdb33dd35491935e80f71c40c576b536df4223eb98d5f7ccd9643293"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
