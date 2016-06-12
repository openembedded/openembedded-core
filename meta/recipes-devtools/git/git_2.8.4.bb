require git.inc

EXTRA_OECONF += "ac_cv_snprintf_returns_bogus=no \
                 ac_cv_fread_reads_directories=${ac_cv_fread_reads_directories=yes} \
                 "
EXTRA_OEMAKE += "NO_GETTEXT=1"

SRC_URI[tarball.md5sum] = "51e5a6b8b7f834b27d91d2268ae10a9c"
SRC_URI[tarball.sha256sum] = "626e319f8a24fc0866167ea5f6bf3e2f38f69d6cb2e59e150f13709ca3ebf301"
SRC_URI[manpages.md5sum] = "c4d17feb686ff9be3d6f8762a89cdafe"
SRC_URI[manpages.sha256sum] = "953a8eadaf4ae96dbad2c3ec12384c677416843917ef83d94b98367ffd55afc0"
