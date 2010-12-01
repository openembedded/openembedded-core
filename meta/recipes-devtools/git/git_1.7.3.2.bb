require git.inc

PR = "r0"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "

SRC_URI[md5sum] = "902f7f07a789fedc0d2ac03656b85969"
SRC_URI[sha256sum] = "bc4cf6bc7c116056050ef43b051691828d7101327b23311d424b02eaee553e71"
