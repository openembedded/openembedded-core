require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "7faff70073c2c4a503cd584071fabb84"
SRC_URI[tarball.sha256sum] = "3cb09a3917c2d8150fc1708f3019cf99a8f0feee6cd61bba3797e3b2a85be9dc"
SRC_URI[manpages.md5sum] = "6f8a2f890e40f18f4c96a5cd70722ad3"
SRC_URI[manpages.sha256sum] = "ac5c600153d1e4a1c6494e250cd27ca288e7667ad8d4ea2f2386f60ba1b78eec"
