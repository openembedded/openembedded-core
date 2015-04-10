require git.inc

SRC_URI[tarball.md5sum] = "8101cd7497ee64d1ed07d12541826a30"
SRC_URI[tarball.sha256sum] = "ab88b66b71bcf1918d57f2cb9d4f4763a63d6628c8c69ab331ba77263da1f096"
SRC_URI[manpages.md5sum] = "209ed840bb155bc6fc129675ee7bb0a2"
SRC_URI[manpages.sha256sum] = "56a1661df8c405b1ca54ee61c6e2327af517a0e14b686abc16480f02fec69819"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
