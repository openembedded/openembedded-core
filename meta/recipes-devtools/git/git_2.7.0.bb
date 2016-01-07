require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "f3c3ed131e37e5e07cc0b81e9b539908"
SRC_URI[tarball.sha256sum] = "db9df4435c70a9b25d20a9ee20d65101692dc907a5a4b30e8343c9ae1b913ead"
SRC_URI[manpages.md5sum] = "dca693846672e68c31d2eb1fc32e763b"
SRC_URI[manpages.sha256sum] = "444703d66d8441c3c67ba12997719d7510b67a6bc51ccd098474c417a334fb61"
