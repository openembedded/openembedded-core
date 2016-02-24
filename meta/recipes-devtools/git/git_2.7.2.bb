require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "162ddc6c9b243899ad67ebd6b1c166b1"
SRC_URI[tarball.sha256sum] = "58959e3ef3046403216a157dfc683c4d7f0dd83365463b8dd87063ded940a0df"
SRC_URI[manpages.md5sum] = "7bb067d6363f537b92c3b8b813ff9ed6"
SRC_URI[manpages.sha256sum] = "e6b5481fd6e24a1d1b155ef17363b313d47025bf6da880737fa872ab78e24f15"
