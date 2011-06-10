require git.inc

PR = "r2"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "

SRC_URI[md5sum] = "a49291116e3b0564e069ae989e4db6fb"
SRC_URI[sha256sum] = "a1d4a1c59300e68fbc493a2cbe9257048d4d6f4363924bf34f38c413a825f80c"
