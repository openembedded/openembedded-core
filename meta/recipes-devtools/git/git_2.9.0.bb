require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "c982d63b69c1206c1b9d28de31520775"
SRC_URI[tarball.sha256sum] = "bff7560f5602fcd8e37669e0f65ef08c6edc996e4f324e4ed6bb8a84765e30bd"
SRC_URI[manpages.md5sum] = "6945b85d17929691327dc12e7d1e0cbd"
SRC_URI[manpages.sha256sum] = "35ba69a8560529aa837e395a6d6c8d42f4d29b40a3c1cc6e3dc69bb1faadb332"
