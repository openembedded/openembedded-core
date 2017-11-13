require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "1ed7298833336c1accb0a7be5a7a2b1b"
SRC_URI[tarball.sha256sum] = "25762cc50103a6a0665c46ea33ceb0578eee01c19b6a08fd393e8608ccbdb3da"
SRC_URI[manpages.md5sum] = "19a0116bcb0779e0bc997c4180018daf"
SRC_URI[manpages.sha256sum] = "3b1b9ebf02d4aa6e741becd1ed9319597488743f939fdab3b894ec52d25408ef"
