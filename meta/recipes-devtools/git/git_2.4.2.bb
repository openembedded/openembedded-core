require git.inc

SRC_URI[tarball.md5sum] = "80e127b1bde921479d1a6c3fa697e385"
SRC_URI[tarball.sha256sum] = "8edd564051fb776a41b4c475ed2d6105de82b674b8903a9efafe8a0633e4876e"
SRC_URI[manpages.md5sum] = "b52251e6f859a54bf5d5a3b7682fbfbf"
SRC_URI[manpages.sha256sum] = "114b22518dd71cc1a16018b0ff198a5ee1e6b2239381dfd6a6b9994f3da89661"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
