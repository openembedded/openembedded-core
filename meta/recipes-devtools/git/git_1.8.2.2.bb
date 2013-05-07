require git.inc

SRC_URI = "http://git-core.googlecode.com/files/git-${PV}.tar.gz"
SRC_URI[md5sum] = "f7407df37facf579dcaa979266cc3c59"
SRC_URI[sha256sum] = "020de38eda302bb6e1be460f7499211948193b306beab4e1a520ffc6f7294794"

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no ac_cv_c_c99_format=yes \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"
