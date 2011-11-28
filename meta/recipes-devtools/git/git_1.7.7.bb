require git.inc

PR = "r2"

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "5d645884e688921e773186783b65ce33"
SRC_URI[sha256sum] = "5a977bc01e4989b9928345e99aab15ce896cf5897c6e32eb449538574df377f6"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "

