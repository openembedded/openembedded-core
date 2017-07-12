require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "894583cddfb0ec3b0156484966f9db9c"
SRC_URI[tarball.sha256sum] = "e19d450648d6d100eb93abaa5d06ffbc778394fb502354b7026d73e9bcbc3160"
SRC_URI[manpages.md5sum] = "97384d23f2ee88d5ce51ffc75096bd3e"
SRC_URI[manpages.sha256sum] = "1c2bd0a2340b2ef118b7b167a8fec6cc05eb18cad9043e6e7a95fd8a70bb8c4c"
