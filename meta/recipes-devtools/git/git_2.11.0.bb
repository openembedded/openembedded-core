require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "eac9324afcf4c95ab11acb2e2283e376"
SRC_URI[tarball.sha256sum] = "d3be9961c799562565f158ce5b836e2b90f38502d3992a115dfb653d7825fd7e"
SRC_URI[manpages.md5sum] = "cc86bfaf75174e3c8a10615e97bf3e55"
SRC_URI[manpages.sha256sum] = "437a0128acd707edce24e1a310ab2f09f9a09ee42de58a8e7641362012dcfe22"
