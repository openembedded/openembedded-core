require git.inc

PR = "r1"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "

SRC_URI[md5sum] = "da6c8f4967393342a4397b955db72cb1"
SRC_URI[sha256sum] = "44b3ebc87d83b34468718fb6fa424420877e1f66c190389d693d090662a3556d"
