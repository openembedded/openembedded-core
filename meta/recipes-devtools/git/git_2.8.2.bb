require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "3022d8ebf64b35b9704d5adf54b256f9"
SRC_URI[tarball.sha256sum] = "a029c37ee2e0bb1efea5c4af827ff5afdb3356ec42fc19c1d40216d99e97e148"
SRC_URI[manpages.md5sum] = "8c9da11aeb45b4a286ba766e25aaf3ab"
SRC_URI[manpages.sha256sum] = "82d322211aade626d1eb3bcf3b76730bfdd2fcc9c189950fb0a8bdd69c383e2f"
