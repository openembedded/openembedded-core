require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "3f210b2dd1613d90ded3b8732b052025"
SRC_URI[tarball.sha256sum] = "94faf2c0b02a7920b0b46f4961d8e9cad08e81418614102898a55f980fa3e7e4"
SRC_URI[manpages.md5sum] = "8251512320568a049192a8219c51e905"
SRC_URI[manpages.sha256sum] = "6cf38ab3ad43ccdcd6a73ffdcf2a016d56ab6b4b240a574b0bb96f520a04ff55"
