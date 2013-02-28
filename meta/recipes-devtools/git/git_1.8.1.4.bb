require git.inc

PR = "r0"

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "60f32ef4a6b3fa2143b81a28704333ed"
SRC_URI[sha256sum] = "cbb88cfab66c88189ac93a9cfdeebdd2b2fdf833848fbec0c566278889c17de1"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
